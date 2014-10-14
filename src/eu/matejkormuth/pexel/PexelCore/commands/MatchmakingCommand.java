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
