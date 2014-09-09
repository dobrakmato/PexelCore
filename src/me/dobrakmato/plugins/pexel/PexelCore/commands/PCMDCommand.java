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

import me.dobrakmato.plugins.pexel.ColorWar.ColorWarArena;
import me.dobrakmato.plugins.pexel.ColorWar.ColorWarMinigame;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.TntTag.TntTagArena;
import me.dobrakmato.plugins.pexel.TntTag.TntTagMinigame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * PCMD (internal pexel command) executor.
 * 
 * @author Mato Kormuth
 * 
 */
public class PCMDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
            final String label, final String[] args) {
        if (sender instanceof Player) {
            Player psender = (Player) sender;
            if (args.length == 1) {
                String arg_command = args[0];
                if (arg_command.equalsIgnoreCase("cwtest")) {
                    ColorWarArena arena = ((ColorWarMinigame) StorageEngine.getMinigame("colorwar")).trrtrtr();
                    if (arena.canJoin()) {
                        sender.sendMessage(ChatColor.GREEN + "Joining ColorWar...");
                        arena.onPlayerJoin(psender);
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Arena is in progress now!");
                    }
                }
                else if (arg_command.equalsIgnoreCase("tnttest")) {
                    TntTagArena arena = ((TntTagMinigame) StorageEngine.getMinigame("tnttag")).trrtrtr();
                    if (arena.canJoin()) {
                        sender.sendMessage(ChatColor.GREEN + "Joining TntTag...");
                        arena.onPlayerJoin(psender);
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Arena is in progress now!");
                    }
                }
            }
        }
        return true;
    }
    
}
