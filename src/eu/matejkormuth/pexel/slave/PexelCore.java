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
package eu.matejkormuth.pexel.slave;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.sun.net.httpserver.HttpServer;

import eu.matejkormuth.pexel.slave.areas.Areas;
import eu.matejkormuth.pexel.slave.bans.BanListServer;
import eu.matejkormuth.pexel.slave.bans.BanStorage;
import eu.matejkormuth.pexel.slave.commands.AlternativeCommands;
import eu.matejkormuth.pexel.slave.commands.ArenaCommand;
import eu.matejkormuth.pexel.slave.commands.BukkitCommandManager;
import eu.matejkormuth.pexel.slave.commands.ChannelCommand;
import eu.matejkormuth.pexel.slave.commands.CommandManager;
import eu.matejkormuth.pexel.slave.commands.FriendCommand;
import eu.matejkormuth.pexel.slave.commands.GateCommand;
import eu.matejkormuth.pexel.slave.commands.LobbyCommand;
import eu.matejkormuth.pexel.slave.commands.MatchmakingCommand;
import eu.matejkormuth.pexel.slave.commands.PCMDCommand;
import eu.matejkormuth.pexel.slave.commands.PartyCommand;
import eu.matejkormuth.pexel.slave.commands.QJCommand;
import eu.matejkormuth.pexel.slave.commands.SettingsCommand;
import eu.matejkormuth.pexel.slave.commands.SpawnCommand;
import eu.matejkormuth.pexel.slave.commands.UnfriendCommand;
import eu.matejkormuth.pexel.slave.core.Achievements;
import eu.matejkormuth.pexel.slave.core.Auth;
import eu.matejkormuth.pexel.slave.core.AutoMessage;
import eu.matejkormuth.pexel.slave.core.License;
import eu.matejkormuth.pexel.slave.core.Log;
import eu.matejkormuth.pexel.slave.core.MagicClock;
import eu.matejkormuth.pexel.slave.core.Scheduler;
import eu.matejkormuth.pexel.slave.core.StorageEngine;
import eu.matejkormuth.pexel.slave.core.UpdatedParts;
import eu.matejkormuth.pexel.slave.matchmaking.Matchmaking;
import eu.matejkormuth.pexel.slave.matchmaking.MatchmakingSignUpdater;
import eu.matejkormuth.pexel.slave.util.AsyncWorker;
import eu.matejkormuth.pexel.slave.util.PlayerFreezer;
import eu.matejkormuth.pexel.PexelNetworking.PexelMasterServer;
import eu.matejkormuth.pexel.PexelNetworking.PexelServerClient;

/**
 * Bukkit plugin class.
 */
public class PexelCore extends JavaPlugin implements PluginMessageListener {
    /**
     * Pexel matchmaking.
     */
    public Matchmaking            matchmaking;
    /**
     * Player freezer.
     */
    public PlayerFreezer          freezer;
    /**
     * Eent processor.
     */
    public EventProcessor         eventProcessor;
    /**
     * Magic clock instance.
     */
    public MagicClock             magicClock;
    /**
     * AutoMessage instance.
     */
    public AutoMessage            message;
    /**
     * Master server instance.
     */
    public PexelMasterServer      pexelserver;
    /**
     * Master server client instance.
     */
    public PexelServerClient      pexelclient;
    /**
     * AsyncWorker object.
     */
    public AsyncWorker            asyncWorker;
    /**
     * Pexel auth object.
     */
    public Auth                   auth;
    /**
     * Pexel scheduler object.
     */
    public Scheduler              scheduler;
    /**
     * Pexel command manager.
     */
    public CommandManager         commandManager;
    /**
     * Pexel Ban storage.
     */
    public BanStorage             banStorage;
    public BanListServer          banListServer;
    public HttpServer             serv;
    public Achievements           achievementsClient;
    
    /**
     * Pexel matchmaking sign updater.
     */
    public MatchmakingSignUpdater matchmakingSignUpdater;
    
    @SuppressWarnings("deprecation")
    @Override
    public void onDisable() {
        Log.partDisable("Core");
        //Shutdown all updated parts.
        UpdatedParts.shutdown();
        
        this.pexelserver.close();
        this.banListServer.stop();
        this.banStorage.save();
        this.serv.stop(0);
        
        this.matchmakingSignUpdater.stop();
        
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
        
        try {
            this.loadLibs();
        } catch (MalformedURLException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        
        // Print license to console.
        License.print();
        
        Pexel.initialize(this);
        this.createDirectoryStructure();
        
        this.freezer = new PlayerFreezer();
        
        this.scheduler = new Scheduler();
        
        try {
            this.pexelserver = new PexelMasterServer(30789);
            this.pexelserver.listen();
            
            this.banListServer = new BanListServer();
            
            this.serv = HttpServer.create(new InetSocketAddress(35000), 50);
            this.serv.createContext("/games", new Matchmaking.Handler());
            this.serv.setExecutor(null);
            this.serv.start();
        } catch (Exception e) {
            
        }
        
        this.message = new AutoMessage();
        this.message.updateStart();
        
        this.banStorage = new BanStorage();
        this.banStorage.load();
        
        this.auth = new Auth();
        
        this.matchmaking = new Matchmaking();
        this.matchmaking.updateStart();
        
        this.achievementsClient = new Achievements();
        
        try {
            this.matchmakingSignUpdater = new MatchmakingSignUpdater();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
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
        this.commandManager.registerCommands(new MatchmakingCommand());
        
        StorageEngine.initialize(this);
        StorageEngine.loadData();
        
        Log.___prblm_stp();
        
        new AlternativeCommands();
        
        try {
            this.pexelclient = new PexelServerClient("127.0.0.1", 30789);
        } catch (Exception e) {
        }
        
        HardCoded.main();
        
        try {
            new PNBroadcastServer();
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("PNB-Service: " + e.toString());
        }
        
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }
    
    private void loadLibs() throws MalformedURLException, ClassNotFoundException {
        Log.info("Loading external libraries...");
        
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?>[] parameters = new Class[] { URL.class };
        Class<?> sysclass = URLClassLoader.class;
        
        for (File f : new File(this.getDataFolder().getAbsolutePath() + "/libs").listFiles()) {
            try {
                Log.info("[P-LIBLOAD] Loading " + f.getAbsolutePath());
                Method method = sysclass.getDeclaredMethod("addURL", parameters);
                method.setAccessible(true);
                method.invoke(sysloader, new Object[] { f.toURI().toURL() });
            } catch (Throwable t) {
                t.printStackTrace();
                Log.severe("Error, could not add URL (" + f.getAbsolutePath()
                        + ") to system classloader");
            }
        }
    }
    
    private void createDirectoryStructure() {
        boolean created = false;
        String path = this.getDataFolder().getAbsolutePath();
        created |= new File(path + "/arenas").mkdirs();
        created |= new File(path + "/cache").mkdirs();
        created |= new File(path + "/records").mkdirs();
        created |= new File(path + "/profiles").mkdirs();
        created |= new File(path + "/clips").mkdirs();
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
