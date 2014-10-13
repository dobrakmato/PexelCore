package eu.matejkormuth.pexel.PexelCore.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.arenas.SimpleArena;
import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;
import eu.matejkormuth.pexel.PexelCore.core.Party;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.matchmaking.MatchmakingRequest;

@CommandHandler(name = "matchmaking")
public class MatchmakingCommand {
    @SubCommand
    public void main(final Player sender) {
        sender.sendMessage(ChatColor.YELLOW + "Cool! Now try /matchmaking help!");
    }
    
    @SubCommand(description = "joins specified arena")
    public void joinarena(final Player sender, final String arenaname) {
        SimpleArena arena = StorageEngine.getArena(arenaname);
        if (arena != null) {
            if (StorageEngine.getProfile(sender.getUniqueId()).getParty() != null) {
                Party party = StorageEngine.getProfile(sender.getUniqueId()).getParty();
                if (party.isOwner(sender)) {
                    for (Player p : party.getPlayers())
                        p.sendMessage(ChatColor.YELLOW
                                + "Your party joined matchmaking!");
                    Pexel.getMatchmaking().registerRequest(
                            party.toRequest(arena.getMinigame(), arena));
                }
                else {
                    sender.sendMessage(ChatManager.error("You cannot join games while you are in party!"));
                }
            }
            else {
                sender.sendMessage(ChatColor.YELLOW + "Your have joined matchmaking!");
                MatchmakingRequest request = new MatchmakingRequest(
                        Arrays.asList(sender), arena.getMinigame(), arena);
                Pexel.getMatchmaking().registerRequest(request);
            }
        }
        else {
            sender.sendMessage(ChatManager.error("You cannot join games while you are in party!"));
        }
    }
    
    @SubCommand(description = "joins random arena with specified minigame")
    public void joingame(final Player sender, final String minigame) {
        if (StorageEngine.getProfile(sender.getUniqueId()).getParty() != null) {
            Party party = StorageEngine.getProfile(sender.getUniqueId()).getParty();
            if (party.isOwner(sender)) {
                for (Player p : party.getPlayers())
                    p.sendMessage(ChatColor.YELLOW + "Your party joined matchmaking!");
                Pexel.getMatchmaking().registerRequest(
                        party.toRequest(StorageEngine.getMinigame(minigame), null));
            }
            else {
                sender.sendMessage(ChatManager.error("You cannot join games while you are in party!"));
            }
        }
        else {
            sender.sendMessage(ChatColor.YELLOW + "Your have joined matchmaking!");
            MatchmakingRequest request = new MatchmakingRequest(Arrays.asList(sender),
                    StorageEngine.getMinigame(minigame), null);
            Pexel.getMatchmaking().registerRequest(request);
        }
    }
}
