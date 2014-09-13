package me.dobrakmato.plugins.pexel.PexelCore.commands;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Command manager class that supports bukkit commands API.
 */
public class BukkitCommandManager extends CommandManager implements Listener {
    /**
     * Initializes new instance of {@link CommandManager} that supports bukkit.
     */
    public BukkitCommandManager() {
        Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
    }
    
    @EventHandler
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/")) {
            this.parseCommand(event.getPlayer(), event.getMessage().substring(1));
        }
        else {
            this.parseCommand(event.getPlayer(), event.getMessage());
        }
    }
}
