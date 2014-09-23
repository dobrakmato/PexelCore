package eu.matejkormuth.pexel.PexelCore.util;

import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.arenas.AdvancedMinigameArena;
import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;

/**
 * Formats and send messages to network (global) chat channel.
 */
public class NetworkCCFormatter {
    private static final String SEPARATOR            = "|";
    
    private static final int    MSG_TYPE_CONSTRUCTOR = 0;
    private static final int    MSG_TYPE_CD_START    = 0;
    private static final int    MSG_TYPE_CD_STOP     = 0;
    private static final int    MSG_TYPE_PLAYERJOIN  = 0;
    private static final int    MSG_TYPE_PLAYERLEAVE = 0;
    private static final int    MSG_TYPE_PLAYERCOUNT = 0;
    
    public static final void sendArenaMsg(final int type,
            final AdvancedMinigameArena arena, final String msg) {
        ChatManager.CHANNEL_NETWORK.broadcastMessage("ARENA-" + type
                + NetworkCCFormatter.SEPARATOR + NetworkCCFormatter.formatArena(arena)
                + NetworkCCFormatter.SEPARATOR + msg);
    }
    
    private static String formatArena(final AdvancedMinigameArena arena) {
        return arena.getName();
    }
    
    /**
     * Sends message to network chat channel.
     * 
     * @param type
     *            type of message - one of MSG_TYPE_* constants from {@link NetworkCCFormatter}
     * @param arena
     *            arena that executed this send
     * @param msg
     *            message to send
     */
    public static final void send(final int type, final AdvancedMinigameArena arena,
            final String msg) {
        NetworkCCFormatter.sendArenaMsg(type, arena, msg);
    }
    
    public static void sendConstructor(final AdvancedMinigameArena advancedMinigameArena) {
        NetworkCCFormatter.sendArenaMsg(MSG_TYPE_CONSTRUCTOR, advancedMinigameArena,
                "Construct");
    }
    
    public static void sendCDstart(final AdvancedMinigameArena advancedMinigameArena) {
        NetworkCCFormatter.sendArenaMsg(MSG_TYPE_CD_START, advancedMinigameArena,
                Long.toString(System.currentTimeMillis()));
    }
    
    public static void sendCDstop(final AdvancedMinigameArena advancedMinigameArena) {
        NetworkCCFormatter.sendArenaMsg(MSG_TYPE_CD_STOP, advancedMinigameArena,
                Long.toString(System.currentTimeMillis()));
    }
    
    public static void sendPlayerJoin(final AdvancedMinigameArena advancedMinigameArena,
            final Player player) {
        NetworkCCFormatter.sendArenaMsg(MSG_TYPE_PLAYERJOIN, advancedMinigameArena,
                player.getUniqueId().toString() + "/" + player.getName());
        NetworkCCFormatter.send(MSG_TYPE_PLAYERCOUNT, advancedMinigameArena,
                Integer.toString(advancedMinigameArena.getPlayerCount()));
    }
    
    public static void sendPlayerLeft(final AdvancedMinigameArena advancedMinigameArena,
            final Player player) {
        NetworkCCFormatter.sendArenaMsg(MSG_TYPE_PLAYERLEAVE, advancedMinigameArena,
                player.getUniqueId().toString() + "/" + player.getName());
        NetworkCCFormatter.send(MSG_TYPE_PLAYERCOUNT, advancedMinigameArena,
                Integer.toString(advancedMinigameArena.getPlayerCount()));
    }
}
