package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Used for handling protected areas.
 * 
 * @author Mato Kormuth
 * 
 */
public class ProtectedArea
{
	/**
	 * Map of default values for flags.
	 */
	public static final Map<AreaFlag, Boolean>				defaultFlags	= new HashMap<AreaFlag, Boolean>();
	
	//Initialization of static values.
	static
	{
		ProtectedArea.defaultFlags.put(AreaFlag.BLOCK_BREAK, false);
		ProtectedArea.defaultFlags.put(AreaFlag.BLOCK_PLACE, false);
		ProtectedArea.defaultFlags.put(AreaFlag.PLAYER_GETDAMAGE, false);
		ProtectedArea.defaultFlags.put(AreaFlag.PLAYER_DODAMAGE, false);
		ProtectedArea.defaultFlags.put(AreaFlag.PLAYER_DROPITEM, false);
		ProtectedArea.defaultFlags.put(AreaFlag.PLAYER_STARVATION, false);
		ProtectedArea.defaultFlags.put(AreaFlag.AREA_CHAT_GOODBYE, true);
		ProtectedArea.defaultFlags.put(AreaFlag.AREA_CHAT_PERMISSIONDENIED,
				true);
		ProtectedArea.defaultFlags.put(AreaFlag.AREA_CHAT_WELCOME, true);
	}
	
	/**
	 * Area region
	 */
	protected Region										region;
	/**
	 * Global area flags.
	 */
	protected final Map<AreaFlag, Boolean>					globalFlags		= new HashMap<AreaFlag, Boolean>();
	/**
	 * Player area flags.
	 */
	protected final Map<UUID, HashMap<AreaFlag, Boolean>>	playerFlags		= new HashMap<UUID, HashMap<AreaFlag, Boolean>>();
	/**
	 * Owner of area.
	 */
	protected AreaOwner										owner;
	/**
	 * Name of area.
	 */
	protected final String									areaName;
	
	/**
	 * Creates new area with specified name.
	 * 
	 * @param name
	 */
	public ProtectedArea(final String name)
	{
		this.areaName = name;
	}
	
	/**
	 * Returns value of global flag. If not specified uses parent's flag (default).
	 * 
	 * @param flag
	 * @return
	 */
	public boolean getGlobalFlag(final AreaFlag flag)
	{
		return this.globalFlags.get(flag)
				| ProtectedArea.defaultFlags.get(flag) | false;
	}
	
	/**
	 * Returns value of player flag. If not specified uses parent's flag (global).
	 * 
	 * @param flag
	 * @param player
	 * @return
	 */
	public boolean getPlayerFlag(final AreaFlag flag, final UUID player)
	{
		if (this.playerFlags.containsKey(player))
			return this.playerFlags.get(player).get(flag)
					| this.globalFlags.get(flag)
					| ProtectedArea.defaultFlags.get(flag) | false;
		else
			return this.globalFlags.get(flag)
					| ProtectedArea.defaultFlags.get(flag) | false;
	}
	
	/**
	 * Sets global flag.
	 * 
	 * @param flag
	 * @param value
	 */
	public void setGlobalFlag(final AreaFlag flag, final boolean value)
	{
		this.globalFlags.put(flag, value);
	}
	
	/**
	 * Sets player flag.
	 * 
	 * @param flag
	 * @param value
	 * @param player
	 */
	public void setPlayerFlag(final AreaFlag flag, final boolean value,
			final UUID player)
	{
		if (this.playerFlags.containsKey(player))
			this.playerFlags.get(player).put(flag, value);
		else
		{
			this.playerFlags.put(player, new HashMap<AreaFlag, Boolean>());
			this.playerFlags.get(player).put(flag, value);
		}
	}
	
	/**
	 * Returns area region.
	 * 
	 * @return
	 */
	public Region getRegion()
	{
		return this.region;
	}
	
	/**
	 * Returns area name.
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.areaName;
	}
	
	/**
	 * Returns area owner.
	 * 
	 * @return
	 */
	public AreaOwner getOwner()
	{
		return this.owner;
	}
}
