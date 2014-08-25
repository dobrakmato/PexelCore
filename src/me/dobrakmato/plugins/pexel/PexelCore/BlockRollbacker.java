package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Block rollbacker.
 * 
 * @author Mato Kormuth
 * 
 */
public class BlockRollbacker
{
	private final List<BlockChange>	changes				= new ArrayList<BlockChange>();
	private final List<Location>	changeLocations		= new ArrayList<Location>();
	private int						taskId				= 0;
	private Runnable				onFinished;
	private int						maxChangesPerTick	= 128;
	
	/**
	 * Registers a change to this rollbacker.
	 * 
	 * @param change
	 */
	public void addChange(final BlockChange change)
	{
		if (!this.changeLocations.contains(change.getLocation()))
		{
			this.changes.add(change);
			this.changeLocations.add(change.getLocation());
		}
	}
	
	/**
	 * Reverts all registered changes right after call of this function. (Not recomended)
	 */
	public void rollback()
	{
		for (BlockChange change : this.changes)
		{
			change.applyRollback();
			this.changeLocations.remove(change.getLocation());
		}
	}
	
	/**
	 * Starts a task for rollbacking with default (128) amount of blocks reverted in one tick.
	 * 
	 * @param onFinished
	 *            runnable, that should be called after the rollback is done.
	 */
	public void rollbackAsync(final Runnable onFinished)
	{
		this.onFinished = onFinished;
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				BlockRollbacker.this.doRollback();
			}
		}, 0L, 1L);
	}
	
	/**
	 * Starts a task for rollbacking with specified amount of blocks reverted in one tick.
	 * 
	 * @param onFinished
	 *            runnable, that should be called after the rollback is done.
	 */
	public void rollbackAsync(final Runnable onFinished, final int maxIterations)
	{
		this.maxChangesPerTick = maxIterations;
		this.rollbackAsync(onFinished);
	}
	
	/**
	 * Performs one rollback (rollbacks this.maxChangesPerTick blocks).
	 */
	private void doRollback()
	{
		if (this.changes.size() == 0)
		{
			Bukkit.getScheduler().cancelTask(this.taskId);
			this.onFinished.run();
		}
		else
		{
			int count = 0;
			for (Iterator<BlockChange> iterator = this.changes.iterator(); iterator.hasNext();)
			{
				if (count > this.maxChangesPerTick)
					break;
				else
				{
					BlockChange bc = iterator.next();
					this.changeLocations.remove(bc.getLocation());
					bc.applyRollback();
					iterator.remove();
				}
				count++;
			}
		}
	}
}
