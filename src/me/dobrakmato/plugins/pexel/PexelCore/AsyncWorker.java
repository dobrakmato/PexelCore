package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Async worker for things like network stuff, etc...
 * 
 * @author Mato Kormuth
 * 
 */
public class AsyncWorker implements Runnable
{
	private final Queue<Runnable>	tasks	= new ArrayBlockingQueue<Runnable>(
													50);
	private final List<Thread>		workers	= new ArrayList<Thread>(4);
	private boolean					enabled	= false;
	
	/**
	 * Creates new object with specified amount of wokrker threads.
	 * 
	 * @param workersCount
	 *            count of workers
	 */
	public AsyncWorker(final int workersCount)
	{
		Log.partEnable("AyncWorker");
		this.enabled = true;
		for (int i = 0; i < workersCount; i++)
		{
			Log.info("Setting up worker #" + (i + 1) + "...");
			this.workers.add(new Thread(this));
			this.workers.get(this.workers.size() - 1).setName(
					"AsyncWorker-" + (i + 1));
			this.workers.get(this.workers.size() - 1).start();
		}
	}
	
	/**
	 * Adds specified task to list. Taks should be executed not later then 200 ms.
	 * 
	 * @param runnable
	 *            taks to be executed from other thread.
	 */
	public void addTask(final Runnable runnable)
	{
		this.tasks.add(runnable);
	}
	
	/**
	 * Shutdowns the workers and the logic.
	 */
	public void shutdown()
	{
		Log.partDisable("AsyncWorker");
		this.enabled = false;
	}
	
	@Override
	public void run()
	{
		while (this.enabled)
		{
			Runnable r = this.tasks.poll();
			if (r != null)
				try
				{
					r.run();
				} catch (Exception ex)
				{
					Log.warn("[AsyncWorker] Task generated: " + ex.getMessage());
				}
			else
				try
				{
					Thread.sleep(200);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			
		}
		Log.info(Thread.currentThread().getName() + " died.");
	}
}
