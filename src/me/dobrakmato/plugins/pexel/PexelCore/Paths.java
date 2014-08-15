package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.UUID;

/**
 * Class used for generating paths.
 * 
 * @author Mato Kormuth
 * 
 */
public class Paths
{
	public static final String playerProfile(final UUID uuid)
	{
		return Pexel.getCore().getDataFolder().getAbsolutePath() + "/profiles/"
				+ uuid.toString() + ".yml";
	}
}
