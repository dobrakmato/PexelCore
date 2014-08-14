package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * A TCP server used for comunicating between multiple servers.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelServer implements Runnable
{
	private final int		port			= 41976;
	private ServerSocket	server			= null;
	private boolean			isEnabled		= false;
	private Thread			runningThread	= null;
	
	public void listen()
	{
		Log.partEnable("Server");
		this.isEnabled = true;
		
		try
		{
			this.server = new ServerSocket(this.port);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.runningThread = new Thread(this);
		this.runningThread.setName("PexelServer-Listen");
		this.runningThread.start();
	}
	
	public void close()
	{
		Log.partDisable("Server");
		this.isEnabled = false;
	}
	
	@Override
	public void run()
	{
		Log.info("Listen thread started!");
		while (this.isEnabled)
		{
			try
			{
				final Socket client = this.server.accept();
				new Thread(new Runnable() {
					@Override
					public void run()
					{
						try
						{
							PexelServer.this.processClient(client);
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}).start();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		Log.info("Listen thread ended!");
	}
	
	private void processClient(final Socket client) throws IOException
	{
		//Uz sme vo vlastnom vlakne.
		ObjectInputStream in = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream out = new ObjectOutputStream(
				client.getOutputStream());
		
		//Spracuj prihlasenie.
		@SuppressWarnings("unused") byte loginType = in.readByte(); //1 = administration, 2 = another minigame server
		String password = in.readUTF();
		
		if (password.equalsIgnoreCase("f3ce472eacf26847650a1b2af032df7a"))
		{
			//Prihlasil sa uspesne.
			out.writeUTF("Password ok!");
			//Nasleduje nekonecny loop.
			while (!client.isClosed())
			{
				//Get request type.
				ServerRequestType requestType = ServerRequestType.fromInt(in.readInt());
				
				switch (requestType)
				{
					case BASIC_SERVERINFO:
						//Pocet hracov
						out.writeInt(Bukkit.getOnlinePlayers().length);
						//Zoznam hracov
						for (Player p : Bukkit.getOnlinePlayers())
						{
							out.writeUTF(p.getName());
							out.writeUTF(p.getUniqueId().toString());
						}
						//Pocet slotov
						out.writeInt(Bukkit.getMaxPlayers());
						//Pocet svetov
						out.writeInt(Bukkit.getWorlds().size());
						//Zoznam svetov
						for (World w : Bukkit.getWorlds())
							out.writeUTF(w.getName());
						//Pocet minihier
						out.writeInt(StorageEngine.getMinigamesCount());
						//Zoznam minihier
						for (String minigameName : StorageEngine.getMinigames().keySet())
							out.writeUTF(minigameName);
						//Pocet aren
						out.writeInt(StorageEngine.getMinigameArenasCount());
						//Zoznam aren
						for (String arenaName : StorageEngine.getArenas().keySet())
							out.writeUTF(arenaName);
						break;
					case PLAYER_INFO:
						String uuid = in.readUTF();
						Player player = Bukkit.getPlayer(UUID.fromString(uuid));
						Location loc = player.getLocation();
						//Pozicia
						out.writeDouble(loc.getX());
						out.writeDouble(loc.getY());
						out.writeDouble(loc.getZ());
						out.writeFloat(loc.getYaw());
						out.writeFloat(loc.getPitch());
						out.writeUTF(loc.getWorld().getName());
						//Player profile
						PlayerProfile profile = StorageEngine.getProfile(player.getUniqueId());
						//Friend count
						out.writeInt(profile.getFriends().size());
						//Friends
						for (UUID friend : profile.getFriends())
							out.writeUTF(friend.toString());
						//Server location
						out.writeUTF(profile.getServerLocation().toString());
					case ARENA_INFO_BASIC:
						String arenaName = in.readUTF();
						MinigameArena arena = StorageEngine.getArena(arenaName);
						//State
						out.writeUTF(arena.getState().toString());
						//Occupied slots
						out.writeInt(arena.playerCount());
						//Max slots
						out.writeInt(arena.getMaximumSlots());
						//Owner
						out.writeUTF(arena.getOwner().toString());
						//Region
						break;
					case ARENA_INFO_REFLECTION:
						String arenaName2 = in.readUTF();
						MinigameArena arena2 = StorageEngine.getArena(arenaName2);
						//Field count
						out.writeInt(arena2.getClass().getDeclaredFields().length);
						//Field list
						for (Field f : arena2.getClass().getDeclaredFields())
						{
							out.writeUTF(f.getName());
							try
							{
								out.writeUTF(f.get(arena2).toString());
							} catch (IllegalArgumentException
									| IllegalAccessException e)
							{
								out.writeUTF("N/A");
							}
						}
						break;
					default:
						break;
				}
			}
		}
		else
		{
			//Zle heslo.
			out.writeUTF("Disconnect! Wrong password!");
			client.close();
		}
	}
}
