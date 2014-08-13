package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * @author Mato Kormuth
 * 
 */
public class JavaInventoryMenuAction implements InventoryMenuAction
{
	private final ParametrizedRunnable	runnable;
	
	public JavaInventoryMenuAction(final ParametrizedRunnable runnable)
	{
		this.runnable = runnable;
	}
	
	@Override
	public void load(final String string)
	{
		
	}
	
	@Override
	public String save()
	{
		return null;
	}
	
	@Override
	public void execute(final Player player)
	{
		this.runnable.run(player);
	}
}
