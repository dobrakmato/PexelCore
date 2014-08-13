package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Location;

/**
 * Class for work with areas.
 * 
 * @author Mato Kormuth
 * 
 */
public class Areas
{
	public static final ProtectedArea findArea(final Location location)
	{
		for (ProtectedArea area : StorageEngine.getAreas().values())
		{
			if (area.getRegion().intersects(location))
				return area;
		}
		return null;
	}
	
	public static final ProtectedArea getArea(final String name)
	{
		return StorageEngine.getAreas().get(name);
	}
}
