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
package me.dobrakmato.plugins.pexel.PexelCore.commands;

import me.dobrakmato.plugins.pexel.PexelCore.areas.Lobby;
import me.dobrakmato.plugins.pexel.PexelCore.chat.ChatManager;
import me.dobrakmato.plugins.pexel.PexelCore.core.Region;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * Class used for evaulating /lobby commands.
 * 
 * @author Mato Kormuth
 * 
 */
public class LobbyCommand implements CommandExecutor {
    private final WorldEditPlugin we;
    
    public LobbyCommand() {
        this.we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin(
                "WorldEdit");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
            final String label, final String[] args) {
        if (command.getName().equalsIgnoreCase("lobbyarena")) {
            if (sender instanceof Player) {
                if (sender.isOp()) {
                    this.processOpCommand((Player) sender, args);
                }
                else {
                    this.processCommand((Player) sender, args);
                }
            }
            else {
                sender.sendMessage(ChatManager.error("This command is only avaiable for players!"));
            }
            return true;
        }
        sender.sendMessage(ChatManager.error("Wrong use!"));
        return true;
    }
    
    private void processCommand(final Player sender, final String[] args) {
        sender.sendMessage(ChatManager.error("Permission denied!"));
    }
    
    private void processOpCommand(final Player sender, final String[] args) {
        if (args.length >= 2) {
            String actionName = args[0];
            String lobbyName = args[1];
            
            if (actionName.equalsIgnoreCase("create")) {
                if (this.checkSelection(sender)) {
                    Region region = new Region(this.we.getSelection(sender));
                    StorageEngine.addLobby(new Lobby(lobbyName, region));
                    sender.sendMessage(ChatManager.success("Lobby '" + lobbyName
                            + "' has been created!"));
                }
            }
            else if (actionName.equalsIgnoreCase("setspawn")) {
                StorageEngine.getLobby(lobbyName).setSpawn(sender.getLocation());
                sender.sendMessage(ChatManager.success("Spawn of lobby '" + lobbyName
                        + "' has been set to your position."));
            }
            else {
                sender.sendMessage(ChatManager.error("Invalid action!"));
            }
        }
        else {
            sender.sendMessage(ChatManager.error("/lobby create <name>"));
            sender.sendMessage(ChatManager.error("/lobby setspawn <name>"));
        }
    }
    
    private boolean checkSelection(final Player sender) {
        if (this.we.getSelection(sender) != null)
            return true;
        else {
            sender.sendMessage(ChatManager.error("Make a WorldEdit selection first!"));
            return false;
        }
    }
}
