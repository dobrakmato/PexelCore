package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Parametrized runnable. Well, i don't know if this works properly...
 * 
 * @author Mato Kormuth
 * 
 */
public abstract class ParametrizedRunnable implements Runnable
{
	private final Object[]	args;
	
	public ParametrizedRunnable(final Object... args)
	{
		this.args = args;
	}
	
	public abstract void run(final Object... args);
	
	@Override
	public void run()
	{
		this.run(this.args);
	}
}
