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
	private final int			maxPlayers;
	
	/**
	 * Creates a new Team with specified team color and team name.
	 * 
	 * @param color
	 *            color of team
	 * @param name
	 *            name of team
	 * @param maximumPlayers
	 *            maximum amount of players in team
	 */
	public Team(final Color color, final String name, final int maximumPlayers)
	{
		this.color = color;
		this.name = name;
		this.maxPlayers = maximumPlayers;
		this.teamchat.setPrefix(ChatColor.YELLOW + "[TEAM]");
	}
	
	/**
	 * Teleports all players.
	 * 
	 * @param loc
	 *            location, to teleport to players
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
	 *            player to add
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
	 *            players to add
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
	 *            player to remove
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
	 * @return team chat channel
	 */
	public ChatChannel getTeamChannel()
	{
		return this.teamchat;
	}
	
	/**
	 * Returns name of the team.
	 * 
	 * @return name of team
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
	
	/**
	 * Retruns list of players.
	 * 
	 * @return list of players in this team
	 */
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	/**
	 * Returns if this team contains specified player.
	 * 
	 * @param p
	 *            specified player
	 * @return true or false
	 */
	public boolean contains(final Player p)
	{
		return this.players.contains(p);
	}
	
	/**
	 * Returns player count.
	 * 
	 * @return amount of players
	 */
	public int getPlayerCount()
	{
		return this.players.size();
	}
	
	/**
	 * Get maximum number of players in team.
	 * 
	 * @return amount of players
	 */
	public int getMaximumPlayers()
	{
		return this.maxPlayers;
	}
	
	/**
	 * Returns whether one player can join this team.
	 * 
	 * @return boolean, if player can join
	 */
	public boolean canJoin()
	{
		return this.players.size() < this.maxPlayers;
	}
	
	/**
	 * Returns whether specified amount of players can join this team.
	 * 
	 * @param amount
	 *            amount of players
	 * @return true, if they can, else false
	 */
	public boolean canJoin(final int amount)
	{
		return this.players.size() + amount < this.maxPlayers;
	}
	
	/**
	 * Retrun's this team color.
	 * 
	 * @return
	 */
	public Color getColor()
	{
		return this.color;
	}
}
