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
package eu.matejkormuth.pexel.PexelCore;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import eu.matejkormuth.pexel.PexelCore.areas.Areas;
import eu.matejkormuth.pexel.PexelCore.commands.AlternativeCommands;
import eu.matejkormuth.pexel.PexelCore.commands.ArenaCommand;
import eu.matejkormuth.pexel.PexelCore.commands.BukkitCommandManager;
import eu.matejkormuth.pexel.PexelCore.commands.ChannelCommand;
import eu.matejkormuth.pexel.PexelCore.commands.CommandManager;
import eu.matejkormuth.pexel.PexelCore.commands.FriendCommand;
import eu.matejkormuth.pexel.PexelCore.commands.GateCommand;
import eu.matejkormuth.pexel.PexelCore.commands.LobbyCommand;
import eu.matejkormuth.pexel.PexelCore.commands.PCMDCommand;
import eu.matejkormuth.pexel.PexelCore.commands.PartyCommand;
import eu.matejkormuth.pexel.PexelCore.commands.QJCommand;
import eu.matejkormuth.pexel.PexelCore.commands.SettingsCommand;
import eu.matejkormuth.pexel.PexelCore.commands.SpawnCommand;
import eu.matejkormuth.pexel.PexelCore.commands.UnfriendCommand;
import eu.matejkormuth.pexel.PexelCore.core.Auth;
import eu.matejkormuth.pexel.PexelCore.core.AutoMessage;
import eu.matejkormuth.pexel.PexelCore.core.License;
import eu.matejkormuth.pexel.PexelCore.core.Log;
import eu.matejkormuth.pexel.PexelCore.core.MagicClock;
import eu.matejkormuth.pexel.PexelCore.core.Scheduler;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.core.UpdatedParts;
import eu.matejkormuth.pexel.PexelCore.matchmaking.Matchmaking;
import eu.matejkormuth.pexel.PexelCore.util.AsyncWorker;
import eu.matejkormuth.pexel.PexelCore.util.PlayerFreezer;
import eu.matejkormuth.pexel.PexelNetworking.PexelMasterServer;
import eu.matejkormuth.pexel.PexelNetworking.PexelServerClient;

/**
 * Hlavna trieda Pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelCore extends JavaPlugin implements PluginMessageListener {
    /**
     * Pexel matchmaking.
     */
    public Matchmaking       matchmaking;
    /**
     * Player freezer.
     */
    public PlayerFreezer     freezer;
    /**
     * Eent processor.
     */
    public EventProcessor    eventProcessor;
    /**
     * Magic clock instance.
     */
    public MagicClock        magicClock;
    /**
     * AutoMessage instance.
     */
    public AutoMessage       message;
    /**
     * Master server instance.
     */
    public PexelMasterServer pexelserver;
    /**
     * Master server client instance.
     */
    public PexelServerClient pexelclient;
    /**
     * AsyncWorker object.
     */
    public AsyncWorker       asyncWorker;
    /**
     * Pexel auth object.
     */
    public Auth              auth;
    /**
     * Pexel scheduler object.
     */
    public Scheduler         scheduler;
    /**
     * Pexel command manager.
     */
    public CommandManager    commandManager;
    
    @Override
    public void onDisable() {
        Log.partDisable("Core");
        //Shutdown all updated parts.
        UpdatedParts.shutdown();
        
        this.pexelserver.close();
        
        //Save important data.
        StorageEngine.saveData(); //oldway
        
        StorageEngine.saveArenas();
        StorageEngine.saveProfiles();
        
        this.asyncWorker.shutdown();
        
        Log.partDisable("Core");
    }
    
    @Override
    public void onEnable() {
        Log.partEnable("Core");
        
        // Print license to console.
        License.print();
        
        Pexel.initialize(this);
        this.createDirectoryStructure();
        
        this.freezer = new PlayerFreezer();
        
        this.scheduler = new Scheduler();
        
        try {
            this.pexelserver = new PexelMasterServer(30789);
            this.pexelserver.listen();
        } catch (Exception e) {
            
        }
        
        this.message = new AutoMessage();
        this.message.updateStart();
        
        this.auth = new Auth();
        
        this.matchmaking = new Matchmaking();
        this.matchmaking.updateStart();
        
        this.magicClock = new MagicClock();
        
        this.asyncWorker = new AsyncWorker(3);
        this.asyncWorker.start();
        
        this.eventProcessor = new EventProcessor();
        
        // Bukkit way
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
        
        // Pexel way
        this.commandManager = new BukkitCommandManager();
        this.commandManager.registerCommands(new PartyCommand());
        this.commandManager.registerCommands(new ChannelCommand());
        
        StorageEngine.initialize(this);
        StorageEngine.loadData();
        
        Log.___prblm_stp();
        
        new AlternativeCommands();
        
        try {
            this.pexelclient = new PexelServerClient("127.0.0.1", 30789);
        } catch (Exception e) {
        }
        
        HardCoded.main();
        
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }
    
    private void createDirectoryStructure() {
        boolean created = false;
        String path = this.getDataFolder().getAbsolutePath();
        created |= new File(path + "/arenas").mkdirs();
        created |= new File(path + "/cache").mkdirs();
        created |= new File(path + "/profiles").mkdirs();
        if (created)
            Log.info("Directory structure expanded!");
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command,
            final String alias, final String[] args) {
        if (command.getName().equalsIgnoreCase("arena"))
            if (sender instanceof Player)
                if (args.length == 1)
                    if (args[0] == "edit")
                        return Arrays.asList(Areas.findArea(
                                ((Player) sender).getLocation()).getName());
        return null;
    }
    
    @Override
    public void onPluginMessageReceived(final String channel, final Player player,
            final byte[] payload) {
        // Nothing for now...
    }
}
