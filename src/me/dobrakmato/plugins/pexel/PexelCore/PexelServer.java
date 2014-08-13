package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP server used for comunicating between multiple servers.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelServer implements Runnable
{
	private final int		port			= 41976;
	private ServerSocket	server			= null;
	private boolean			isEnabled		= false;
	private Thread			runningThread	= null;
	
	public void listen()
	{
		Log.partEnable("Server");
		this.isEnabled = true;
		
		try
		{
			this.server = new ServerSocket(this.port);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.runningThread = new Thread(this);
		this.runningThread.setName("PexelServer-Listen");
		this.runningThread.start();
	}
	
	public void close()
	{
		Log.partDisable("Server");
		this.isEnabled = false;
	}
	
	@Override
	public void run()
	{
		Log.info("Listen thread started!");
		while (this.isEnabled)
		{
			try
			{
				final Socket client = this.server.accept();
				new Thread(new Runnable() {
					@Override
					public void run()
					{
						PexelServer.this.processClient(client);
					}
				}).start();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		Log.info("Listen thread ended!");
	}
	
	private void processClient(final Socket client)
	{
		//TODO: Komunikacia s podserveremi.
	}
}
