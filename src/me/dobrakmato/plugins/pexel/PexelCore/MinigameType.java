package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Arrays;
import java.util.List;

/**
 * All minigame types / tags.
 * 
 * @author Mato Kormuth
 * 
 */
public enum MinigameType
{
	TNT,
	CREEPER,
	PVP;
	
	public static final List<MinigameType> makeTypes(final MinigameType... types)
	{
		return Arrays.asList(types);
	}
}
