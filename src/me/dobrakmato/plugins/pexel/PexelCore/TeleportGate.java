package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Class representating teleport portal.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeleportGate
{
	private final Region	region;
	private Action			action;
	
	public TeleportGate(final Region region, final Action action)
	{
		this.region = region;
		this.action = action;
	}
	
	public Region getRegion()
	{
		return this.region;
	}
	
	protected void execute(final Player player)
	{
		this.action.execute(player);
		/*
		 * if (this.actionType.equalsIgnoreCase("teleport")) { if (this.isCrossServer()) {
		 * this.targetServer.getClient().sendPacket( new CrossServerTeleportPacket(player, this.actionContent)); } else
		 * { String[] parts = this.actionContent.split(",");
		 * 
		 * try { Double x = Double.parseDouble(parts[0]); Double y = Double.parseDouble(parts[1]); Double z =
		 * Double.parseDouble(parts[2]); Float yaw = Float.parseFloat(parts[3]); Float pitch =
		 * Float.parseFloat(parts[4]); String world = parts[5];
		 * 
		 * player.teleport(new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch)); } catch (Exception ex) {
		 * Log.addProblem("Invalid action at gate (" + this.region.toString() + "): " + this.actionType + " >> " +
		 * this.actionContent); } } } else if (this.actionType.equalsIgnoreCase("command")) {
		 * player.performCommand(this.actionContent.replace("%player%", player.getName())); } else {
		 * Log.addProblem("Invalid action at gate (" + this.region.toString() + "): " + this.actionType + " >> " +
		 * this.actionContent); }
		 */
	}
	
	public Action getAction()
	{
		return this.action;
	}
	
	public void setContent(final Action action)
	{
		this.action = action;
	}
}
