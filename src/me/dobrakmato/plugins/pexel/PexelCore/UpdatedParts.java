package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that turns off all parts when needed.
 * 
 * @author Mato Kormuth
 * 
 */
public class UpdatedParts
{
	private static List<UpdatedPart>	parts	= new ArrayList<UpdatedPart>();
	
	public static void shutdown()
	{
		for (UpdatedPart part : UpdatedParts.parts)
			part.updateStop();
		UpdatedParts.parts.clear();
	}
	
	public static void registerPart(final UpdatedPart part)
	{
		Log.info("[UpdatedParts] Registering: "
				+ part.getClass().getSimpleName());
		UpdatedParts.parts.add(part);
	}
}
