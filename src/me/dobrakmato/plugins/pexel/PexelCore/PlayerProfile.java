package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

/**
 * Object for storing player's friends and unfriends.
 * 
 * @author Mato Kormuth
 * 
 */
public class PlayerProfile
{
	/**
	 * Player's UUID.
	 */
	protected final UUID					player;
	/**
	 * Player's friends.
	 */
	protected final List<UUID>				friends			= new ArrayList<UUID>();
	/**
	 * Player's foes.
	 */
	protected final List<UUID>				foes			= new ArrayList<UUID>();
	/**
	 * Player's settings.
	 */
	protected final Map<Setting, Boolean>	settings		= new HashMap<Setting, Boolean>();
	
	/**
	 * Spectating status.
	 */
	protected boolean						spectating		= false;
	/**
	 * Player's location.
	 */
	protected ServerLocation				serverLocation	= new ServerLocation(
																	"Main Lobby",
																	ServerLocationType.LOBBY);
	private ParticleEffect					particleType;
	
	/**
	 * Creates player profile from Player object.
	 * 
	 * @param player
	 */
	public PlayerProfile(final Player player)
	{
		this.player = player.getUniqueId();
	}
	
	/**
	 * Creates player profile from UUID.
	 * 
	 * @param player
	 */
	public PlayerProfile(final UUID player)
	{
		this.player = player;
	}
	
	/**
	 * Adds friend.
	 * 
	 * @param player
	 */
	public void addFriend(final UUID player)
	{
		this.friends.add(player);
	}
	
	/**
	 * Removes friend.
	 * 
	 * @param player
	 */
	public void removeFriend(final UUID player)
	{
		this.friends.remove(player);
	}
	
	public void addFoe(final UUID player)
	{
		this.foes.add(player);
	}
	
	public void removeFoe(final UUID player)
	{
		this.foes.remove(player);
	}
	
	/**
	 * Returns UUID of profile.
	 * 
	 * @return
	 */
	public UUID getUniqueId()
	{
		return this.player;
	}
	
	/**
	 * Return list of player's friends.
	 * 
	 * @return
	 */
	public List<UUID> getFriends()
	{
		return ImmutableList.copyOf(this.friends);
	}
	
	public List<UUID> getFoes()
	{
		return ImmutableList.copyOf(this.foes);
	}
	
	/**
	 * Sets player's server location.
	 * 
	 * @param location
	 */
	public void setServerLocation(final ServerLocation location)
	{
		this.serverLocation = location;
	}
	
	/**
	 * Returns current player's server location.
	 * 
	 * @return
	 */
	public ServerLocation getServerLocation()
	{
		return this.serverLocation;
	}
	
	/**
	 * Returns whatever is this player friend with the specified.
	 * 
	 * @param uniqueId
	 * @return
	 */
	public boolean isFriend(final UUID uniqueId)
	{
		return this.friends.contains(uniqueId);
	}
	
	public boolean isFoe(final UUID uniqueId)
	{
		return this.foes.contains(uniqueId);
	}
	
	/**
	 * Returns setting value.
	 * 
	 * @param setting
	 * @return
	 */
	public boolean getSetting(final Setting setting)
	{
		if (this.settings.containsKey(setting))
			return this.settings.get(setting);
		else
			return true;
	}
	
	public void setSetting(final Setting setting, final boolean value)
	{
		this.settings.put(setting, value);
	}
	
	/**
	 * Return's whatever is player spectating.
	 * 
	 * @return
	 */
	public boolean isSpectating()
	{
		return this.spectating;
	}
	
	/**
	 * Sets player's spectating state.
	 * 
	 * @param spectating
	 */
	public void setSpectating(final boolean spectating)
	{
		this.spectating = spectating;
	}
	
	/**
	 * @param playerProfile
	 */
	public void save(final String path)
	{
		YamlConfiguration yaml = new YamlConfiguration();
		
		yaml.set("player.uuid", this.player.toString());
		yaml.set("player.friends", this.friends);
		yaml.set("player.foes", this.foes);
		
		try
		{
			yaml.save(new File(path));
		} catch (IOException e)
		{
			Log.addProblem("Can't save player profile: " + e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * @param playerProfile
	 * @return
	 */
	public static PlayerProfile load(final String path)
	{
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new File(
				path));
		UUID uuid = UUID.fromString(yaml.getString("player.uuid"));
		
		PlayerProfile profile = new PlayerProfile(uuid);
		
		List<?> friends = yaml.getList("player.friends");
		List<?> foes = yaml.getList("player.foes");
		
		for (Object obj : friends)
			profile.addFriend(UUID.fromString(obj.toString()));
		for (Object obj : foes)
			profile.addFoe(UUID.fromString(obj.toString()));
		
		return profile;
	}
	
	public void setParticleType(final ParticleEffect effect)
	{
		this.particleType = effect;
	}
	
	public ParticleEffect getParticleType()
	{
		return this.particleType;
	}
}
