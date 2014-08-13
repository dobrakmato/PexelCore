package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Different types of server locations.
 * 
 * @author Mato Kormuth
 * 
 */
public enum ServerLocationType
{
	/**
	 * Player is in lobby.
	 */
	LOBBY,
	/**
	 * Player is in minigame lobby.
	 */
	MINIGAME_LOBBY,
	/**
	 * Player is in minigame (palying).
	 */
	MINIGAME,
	/**
	 * Player is in quickjoin session.
	 */
	QUICKJOIN,
	/**
	 * Player location unknown.
	 */
	UNKNOWN
}
