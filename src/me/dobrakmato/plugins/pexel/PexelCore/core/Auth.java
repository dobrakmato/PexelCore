package me.dobrakmato.plugins.pexel.PexelCore.core;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.chat.ChatManager;

import org.bukkit.entity.Player;

/**
 * Class used for offline mode authentication.
 * 
 * @author Mato Kormuth
 * 
 */
public class Auth {
    public void authenticateCommand(final Player player, final String password) {
        // TODO:  PHP or MYSQL Implementation to integrate with unified login system.
        
        // Unfreeze player.
        Pexel.getFreezer().unfreeze(player);
        
        player.sendMessage(ChatManager.success("Successfully logged in!"));
    }
    
    public void authenticateIp(final Player player, final String hostname) {
        // TODO:  PHP or MYSQL Implementation to integrate with unified login system.
    }
}
