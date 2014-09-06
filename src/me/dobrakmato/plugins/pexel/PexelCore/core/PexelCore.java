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
package me.dobrakmato.plugins.pexel.PexelCore.core;

import java.util.Arrays;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.PexelServer;
import me.dobrakmato.plugins.pexel.PexelCore.areas.Areas;
import me.dobrakmato.plugins.pexel.PexelCore.commands.AlternativeCommands;
import me.dobrakmato.plugins.pexel.PexelCore.commands.ArenaCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.FriendCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.GateCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.LobbyCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.PCMDCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.PartyCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.QJCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.SettingsCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.SpawnCommand;
import me.dobrakmato.plugins.pexel.PexelCore.commands.UnfriendCommand;
import me.dobrakmato.plugins.pexel.PexelCore.matchmaking.Matchmaking;
import me.dobrakmato.plugins.pexel.PexelCore.utils.AsyncWorker;
import me.dobrakmato.plugins.pexel.PexelCore.utils.AutoMessage;
import me.dobrakmato.plugins.pexel.PexelCore.utils.PlayerFreezer;
import me.dobrakmato.plugins.pexel.PexelNetworking.PexelMasterServer;
import me.dobrakmato.plugins.pexel.PexelNetworking.PexelServerClient;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Hlavna trieda Pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelCore extends JavaPlugin implements PluginMessageListener
{
	/**
	 * Pexel matchmaking.
	 */
	public Matchmaking			matchmaking;
	/**
	 * Pexel TCP server.
	 */
	public PexelServer			oldServer;
	/**
	 * Player freezer.
	 */
	public PlayerFreezer		freezer;
	/**
	 * Eent processor.
	 */
	public EventProcessor		eventProcessor;
	/**
	 * Magic clock instance.
	 */
	public MagicClock			magicClock;
	/**
	 * AutoMessage instance.
	 */
	public AutoMessage			message;
	/**
	 * Master server instance.
	 */
	public PexelMasterServer	pexelserver;
	/**
	 * Master server client instance.
	 */
	public PexelServerClient	pexelclient;
	/**
	 * AsyncWorker object
	 */
	public AsyncWorker			asyncWorker;
	
	@Override
	public void onDisable()
	{
		Log.partDisable("Core");
		//Shutdown all updated parts.
		UpdatedParts.shutdown();
		
		this.oldServer.close();
		
		this.pexelserver.close();
		
		this.asyncWorker.shutdown();
		
		StorageEngine.saveData();
		Log.partDisable("Core");
	}
	
	@Override
	public void onEnable()
	{
		Log.partEnable("Core");
		
		Pexel.initialize(this);
		
		this.freezer = new PlayerFreezer();
		
		this.oldServer = new PexelServer();
		this.oldServer.listen();
		
		try
		{
			this.pexelserver = new PexelMasterServer(30789);
		} catch (Exception e)
		{
			
		}
		
		this.message = new AutoMessage();
		this.message.updateStart(this);
		
		this.matchmaking = new Matchmaking();
		this.matchmaking.updateStart(this);
		
		this.magicClock = new MagicClock();
		
		this.asyncWorker = new AsyncWorker(4);
		
		this.eventProcessor = new EventProcessor();
		
		this.getCommand("arena").setExecutor(new ArenaCommand());
		this.getCommand("friend").setExecutor(new FriendCommand());
		this.getCommand("unfriend").setExecutor(new UnfriendCommand());
		this.getCommand("settings").setExecutor(new SettingsCommand());
		this.getCommand("party").setExecutor(new PartyCommand());
		this.getCommand("lobbyarena").setExecutor(new LobbyCommand());
		this.getCommand("qj").setExecutor(new QJCommand());
		this.getCommand("spawn").setExecutor(new SpawnCommand());
		this.getCommand("gate").setExecutor(new GateCommand());
		this.getCommand("pcmd").setExecutor(new PCMDCommand());
		
		StorageEngine.initialize(this);
		StorageEngine.loadData();
		
		new AlternativeCommands();
		
		try
		{
			this.pexelclient = new PexelServerClient("127.0.0.1", 30789);
		} catch (Exception e)
		{
		}
		
		HardCoded.main();
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord",
				this);
	}
	
	@Override
	public List<String> onTabComplete(final CommandSender sender,
			final Command command, final String alias, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("arena"))
			if (sender instanceof Player)
				if (args.length == 1)
					if (args[0] == "edit")
						return Arrays.asList(Areas.findArea(
								((Player) sender).getLocation()).getName());
		return null;
	}
	
	@Override
	public void onPluginMessageReceived(final String channel,
			final Player player, final byte[] payload)
	{
		if (!channel.equals("BungeeCord"))
		{
			return;
		}
		else
		{
			String message = new String(payload);
			if (message.startsWith("PlayerCoun"))
			{
				
			}
			else
			{
				//Log.info("onPluginMessageReceived: " + new String(message));
			}
		}
	}
}
