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
	/**
	 * Tries to find arena by specified lcoation, if not found, returns null.
	 * 
	 * @param location
	 *            location to search for area
	 * @return area that was find at specified location
	 */
	public static final ProtectedArea findArea(final Location location)
	{
		for (ProtectedArea area : StorageEngine.getAreas().values())
			if (area.getRegion().intersects(location))
				return area;
		return null;
	}
	
	/**
	 * Returns area by name.
	 * 
	 * @param name
	 *            name of area
	 * @return protected area by name
	 */
	public static final ProtectedArea getArea(final String name)
	{
		return StorageEngine.getAreas().get(name);
	}
}
