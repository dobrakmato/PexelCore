package eu.matejkormuth.pexel.PexelCore.core;

import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;

/**
 * Class used for offline mode authentication.
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
