package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Class specifing pexel compactibile server.
 * 
 * @author Mato Kormuth
 * 
 */
public class Server
{
	public static final Server		THIS_SERVER	= new Server(null,
														"$#__THIS__#$");
	
	private final PexelServerClient	client;
	private final String			name;
	
	public Server(final PexelServerClient client, final String name)
	{
		this.client = client;
		this.name = name;
	}
	
	public boolean isThis()
	{
		return this.name.equals("$#__THIS__#$");
	}
	
	public PexelServerClient getClient()
	{
		return this.client;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public boolean isConnectedToMaster()
	{
		return this.client.isConnected();
	}
}
