// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
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
