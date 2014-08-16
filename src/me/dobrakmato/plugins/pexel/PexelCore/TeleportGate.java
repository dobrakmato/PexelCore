package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Class representating teleport portal.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeleportGate implements UpdatedPart
{
	private final Region	region;
	private int				taskId;
	private String			actionType;
	private String			actionContent;
	
	public TeleportGate(final Region region, final String actionType,
			final String actionContent)
	{
		this.region = region;
		this.actionContent = actionContent;
		this.actionType = actionType;
		
		this.updateStart(Pexel.getCore());
	}
	
	public Region getRegion()
	{
		return this.region;
	}
	
	@Override
	public void updateStart(final PexelCore plugin)
	{
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				TeleportGate.this.update();
			}
		}, 0L, 20L);
	}
	
	protected void update()
	{
		for (Player player : this.region.getPlayersXYZ())
			this.execute(player);
	}
	
	private void execute(final Player player)
	{
		if (this.actionType.equalsIgnoreCase("teleport"))
		{
			String[] parts = this.actionContent.split(",");
			
			try
			{
				Double x = Double.parseDouble(parts[0]);
				Double y = Double.parseDouble(parts[1]);
				Double z = Double.parseDouble(parts[2]);
				Float yaw = Float.parseFloat(parts[3]);
				Float pitch = Float.parseFloat(parts[4]);
				String world = parts[5];
				
				player.teleport(new Location(Bukkit.getWorld(world), x, y, z,
						yaw, pitch));
			} catch (Exception ex)
			{
				Log.addProblem("Invalid action at gate ("
						+ this.region.toString() + "): " + this.actionType
						+ " >> " + this.actionContent);
			}
		}
		else if (this.actionType.equalsIgnoreCase("command"))
		{
			player.performCommand(this.actionContent.replace("%player%",
					player.getName()));
		}
		else
		{
			Log.addProblem("Invalid action at gate (" + this.region.toString()
					+ "): " + this.actionType + " >> " + this.actionContent);
		}
	}
	
	@Override
	public void updateStop()
	{
		Bukkit.getScheduler().cancelTask(this.taskId);
	}
	
	public String getType()
	{
		return this.actionType;
	}
	
	public String getContent()
	{
		return this.actionContent;
	}
	
	public void setContent(final String actionContent2)
	{
		this.actionContent = actionContent2;
	}
	
	public void setType(final String actionType2)
	{
		this.actionType = actionType2;
	}
}
