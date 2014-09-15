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
package eu.matejkormuth.pexel.PexelCore.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Logger for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class Log {
    /**
     * List of problems, that should admins able to view.
     */
    private final static List<String> problems = new ArrayList<String>();
    
    /**
     * Internal logger.
     */
    private final static Logger       log      = Logger.getLogger("PexelCore");
    
    /**
     * Logs 'info' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void info(final String msg) {
        Log.log.info("[PEXEL] " + msg);
    }
    
    /**
     * Logs 'warn' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void warn(final String msg) {
        Log.log.warning("[PEXEL] " + msg);
    }
    
    /**
     * Logs 'severe' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void severe(final String msg) {
        Log.log.severe("[PEXEL] " + msg);
    }
    
    /**
     * Logs 'partEnable' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void partEnable(final String partName) {
        Log.log.info("[PEXEL] " + "Enabling Pexel-" + partName + "...");
    }
    
    /**
     * Logs 'parnDisable' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void partDisable(final String partName) {
        Log.log.info("[PEXEL] " + "Disabling Pexel-" + partName + "...");
    }
    
    /**
     * Logs 'gameEnable' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void gameEnable(final String gameName) {
        Log.log.info("[PEXEL] " + "Enabling Minigame-" + gameName + "...");
    }
    
    /**
     * Logs 'gameDisable' message.
     * 
     * @param msg
     *            message to log
     */
    public final static void gameDisable(final String gameName) {
        Log.log.info("[PEXEL] " + "Disabling Minigame-" + gameName + "...");
    }
    
    /**
     * Adds problem to list. Problem is added to log and reported to all OP on server.
     * 
     * @param message
     */
    protected final static void addProblem(final String message) {
        Log.problems.add(message);
        
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.isOp())
                p.sendMessage(ChatColor.RED + "[Problem]" + message);
    }
    
    /**
     * Returns list of all problems.
     * 
     * @return
     */
    protected final static List<String> getProblems() {
        return Log.problems;
    }
    
    public static void chat(final String msg) {
        Log.log.info("[CHAT] " + msg);
    }
}
