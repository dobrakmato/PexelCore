package me.dobrakmato.plugins.pexel.PexelCore;

import org.apache.commons.lang.Validate;

/**
 * Location on server.
 * 
 * @author Mato Kormuth
 * 
 */
public class ServerLocation
{
	/**
	 * Name of the location.
	 */
	private final String				locationName;
	/**
	 * Type of the location.
	 */
	private final ServerLocationType	locationType;
	
	/**
	 * Creates new instance of ServerLocation.
	 * 
	 * @param locationName
	 * @param locationType
	 */
	public ServerLocation(final String locationName,
			final ServerLocationType locationType)
	{
		Validate.notNull(locationName);
		Validate.notNull(locationType);
		
		this.locationName = locationName;
		this.locationType = locationType;
	}
	
	/**
	 * Returns name of server location.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return this.locationName;
	}
	
	/**
	 * Returns type of location.
	 * 
	 * @return type
	 */
	public ServerLocationType getType()
	{
		return this.locationType;
	}
	
	@Override
	public String toString()
	{
		return this.locationType.toString() + ": " + this.locationName;
	}
}
