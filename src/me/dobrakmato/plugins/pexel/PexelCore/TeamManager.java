package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Class used for managing teams.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeamManager implements Listener
{
	private final List<Team>			teams		= new ArrayList<Team>();
	private final Map<Location, Team>	signs		= new HashMap<Location, Team>();
	private final int					odchylka	= 1;
	private final MinigameArena			arena;
	
	public TeamManager(final MinigameArena arena)
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
		this.arena = arena;
	}
	
	public void reset()
	{
		this.teams.clear();
		for (Location loc : this.signs.keySet())
			this.updateSign(loc, this.signs.get(loc));
		this.signs.clear();
	}
	
	public void addTeam(final Team team)
	{
		this.teams.add(team);
	}
	
	private void updateSignCache(final Team team)
	{
		for (Location loc : this.signs.keySet())
			if (this.signs.get(loc) == team)
				this.updateSign(loc, team);
	}
	
	public void updateSign(final Location location, final Team team)
	{
		Sign s = (Sign) location.getBlock().getState();
		s.setLine(1, ChatColor.BOLD + team.getName());
		if (team.getMaximumPlayers() == team.getPlayerCount())
			s.setLine(2, ChatColor.RED.toString() + team.getPlayerCount() + "/"
					+ team.getMaximumPlayers() + " players");
		else if (team.getPlayerCount() / team.getMaximumPlayers() > 0.75F)
			s.setLine(2, ChatColor.GOLD.toString() + team.getPlayerCount()
					+ "/" + team.getMaximumPlayers() + " players");
		else
			s.setLine(2, ChatColor.GREEN.toString() + team.getPlayerCount()
					+ "/" + team.getMaximumPlayers() + " players");
		s.update();
	}
	
	@EventHandler
	private void onPlayerInteract(final PlayerInteractEvent event)
	{
		if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
				|| event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK)
			if (event.getClickedBlock().getType() == Material.SIGN_POST
					|| event.getClickedBlock().getType() == Material.SIGN)
				if (this.arena.activePlayers.contains(event.getPlayer()))
					this.signClick(event.getPlayer(), event.getClickedBlock());
	}
	
	private void signClick(final Player player, final Block clickedBlock)
	{
		System.out.println("signClick: p: " + player.getName() + ", cb: "
				+ clickedBlock.getLocation().toVector().toString());
		Sign s = (Sign) clickedBlock;
		String teamName = s.getLine(1);
		
		if (s.getLine(0).contains("[Team]"))
		{
			Team team = null;
			for (Team t : this.teams)
				if (t.getName().equalsIgnoreCase(teamName))
					team = t;
			if (team == null)
				throw new RuntimeException("Team not found in TeamManager: "
						+ teamName);
			
			if (team.getPlayerCount() == team.getMaximumPlayers())
				player.sendMessage(ChatManager.error("This team is full!"));
			else
			{
				if (team.canJoin())
				{
					if (this.playerInTeam(player))
					{
						Team oldTeam = this.getTeam(player);
						oldTeam.removePlayer(player);
						this.updateSignCache(oldTeam);
					}
					
					team.addPlayer(player);
					this.updateSign(clickedBlock.getLocation(), team);
				}
				else
				{
					player.sendMessage(ChatManager.error("You can't join this team at this time!"));
				}
			}
		}
	}
	
	public Team getTeam(final Player player)
	{
		for (Team t : this.teams)
			if (t.contains(player))
				return t;
		return null;
	}
	
	public boolean playerInTeam(final Player player)
	{
		for (Team t : this.teams)
			if (t.contains(player))
				return true;
		return false;
	}
	
	public boolean canJoinTeam(final Team team, final Player player)
	{
		return team.getPlayerCount() > this.getAvarangePlayerCount();
	}
	
	public int getAvarangePlayerCount()
	{
		int allPlayers = this.odchylka;
		for (Team team : this.teams)
			allPlayers += team.getPlayerCount();
		return (int) Math.ceil(allPlayers / this.teams.size());
	}
	
	public void autoJoinTeam(final Player p)
	{
		Team leastCrowdedTeam = this.teams.get(0);
		
		for (Team t : this.teams)
			if (t.getPlayerCount() < leastCrowdedTeam.getPlayerCount())
				leastCrowdedTeam = t;
		
		leastCrowdedTeam.addPlayer(p);
	}
}
