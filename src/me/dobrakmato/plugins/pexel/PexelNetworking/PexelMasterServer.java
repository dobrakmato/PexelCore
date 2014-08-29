package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.Log;

/**
 * Master server.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelMasterServer implements Runnable
{
	private ServerSocket					socket;
	private final List<PexelServerClient>	clients	= new ArrayList<PexelServerClient>();
	
	public PexelMasterServer(final int port)
	{
		Log.partEnable("PMS");
		try
		{
			this.socket = new ServerSocket(port);
			new Thread(this).start();
		} catch (IOException e)
		{
			e.printStackTrace();
			this.socket = null;
		}
	}
	
	public void close()
	{
		Log.partDisable("PMS");
		try
		{
			this.socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		Thread.currentThread().setName("PexelMasterServer-ListenThread");
		
		while (!this.socket.isClosed())
		{
			try
			{
				final Socket client = this.socket.accept();
				//Login
				final String serverName = "";
				
				new Thread(new Runnable() {
					@Override
					public void run()
					{
						Log.info("PMS Client " + serverName
								+ " passed login! Comunicating with him now.");
						Thread.currentThread().setName(
								"PexelMasterServer-ClientThread-" + serverName);
						
						PexelServerClient psc = new PexelServerClient(client);
						PexelMasterServer.this.clients.add(psc);
						
						PexelMasterServer.this.processClient(new Server(psc,
								serverName));
					}
				}).start();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void processClient(final Server server)
	{
		while (!this.socket.isClosed())
		{
			try
			{
				server.getClient().getHandler().handlePacket();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		this.clients.remove(server.getClient());
	}
	
	/**
	 * @param crossServerChatMessage
	 */
	public void broadcast(final PexelPacket packet)
	{
		for (PexelServerClient client : this.clients)
			client.sendPacket(packet);
	}
}
