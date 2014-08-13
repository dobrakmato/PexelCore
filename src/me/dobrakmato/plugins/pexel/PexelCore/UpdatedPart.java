package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Interface used for starting and stopping tasks.
 * 
 * @author Mato Kormuth
 * 
 */
public interface UpdatedPart
{
	/**
	 * Called when part should start it's update logic.
	 * 
	 * @param plugin
	 */
	void updateStart(PexelCore plugin);
	
	/**
	 * Called when part should stop it's update logic.
	 */
	void updateStop();
}
