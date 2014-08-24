package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Class representing a team.
 * 
 * @author Mato Kormuth
 * 
 */
public class Team
{
	private final List<Player>	players		= new ArrayList<Player>();
	private final Color			color;
	private final String		name;
	private final ChatChannel	teamchat	= ChatChannel.createRandom();
	
	/**
	 * Creates a new Team with specified team color and team name.
	 * 
	 * @param color
	 * @param name
	 */
	public Team(final Color color, final String name)
	{
		this.color = color;
		this.name = name;
		this.teamchat.setPrefix(ChatColor.YELLOW + "[TEAM]");
	}
	
	/**
	 * Teleports all players.
	 * 
	 * @param loc
	 */
	public void teleportAll(final Location loc)
	{
		for (Player p : this.players)
			p.teleport(loc);
	}
	
	/**
	 * Applies dyed armor to all players.
	 */
	public void applyArmorAll()
	{
		for (Player p : this.players)
			this.applyArmor(p);
	}
	
	/**
	 * Adds player to team.
	 * 
	 * @param p
	 */
	public void addPlayer(final Player p)
	{
		p.sendMessage(ChatColor.GREEN + "You have joined team '" + this.name
				+ "'!");
		this.players.add(p);
		this.teamchat.subscribe(p, SubscribeMode.READ_WRITE);
	}
	
	/**
	 * Adds players to team.
	 * 
	 * @param players
	 */
	public void addPlayer(final Player... players)
	{
		for (Player p : players)
			this.addPlayer(p);
	}
	
	/**
	 * Removes player from the team.
	 * 
	 * @param p
	 */
	public void removePlayer(final Player p)
	{
		p.sendMessage(ChatColor.GREEN + "You have left team '" + this.name
				+ "'!");
		this.players.remove(p);
		this.teamchat.unsubscribe(p);
	}
	
	/**
	 * Return's this team chat channel.
	 * 
	 * @return
	 */
	public ChatChannel getTeamChannel()
	{
		return this.teamchat;
	}
	
	/**
	 * Returns name of the team.
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	private void applyArmor(final Player p)
	{
		p.getInventory().setHelmet(
				ItemUtils.coloredLetherArmor(Material.LEATHER_HELMET,
						this.color));
		p.getInventory().setChestplate(
				ItemUtils.coloredLetherArmor(Material.LEATHER_CHESTPLATE,
						this.color));
		p.getInventory().setLeggings(
				ItemUtils.coloredLetherArmor(Material.LEATHER_LEGGINGS,
						this.color));
		p.getInventory().setBoots(
				ItemUtils.coloredLetherArmor(Material.LEATHER_BOOTS, this.color));
	}
}
