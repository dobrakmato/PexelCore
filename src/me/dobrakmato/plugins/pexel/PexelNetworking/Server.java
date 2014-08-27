package me.dobrakmato.plugins.pexel.PexelNetworking;

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
	private final String			bungeeName;
	
	/**
	 * Creates new server with same name and bungeename.
	 * 
	 * @param client
	 * @param name
	 */
	public Server(final PexelServerClient client, final String name)
	{
		this.client = client;
		this.name = name;
		this.bungeeName = name;
	}
	
	/**
	 * Creates a new server with different name and bungeename.
	 * 
	 * @param client
	 * @param name
	 * @param bungeeName
	 */
	public Server(final PexelServerClient client, final String name,
			final String bungeeName)
	{
		this.client = client;
		this.name = name;
		this.bungeeName = bungeeName;
	}
	
	public boolean isLocalServer()
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
	
	public String getBungeeName()
	{
		return this.bungeeName;
	}
}
