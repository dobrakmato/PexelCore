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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.areas.AreaFlag;
import eu.matejkormuth.pexel.PexelCore.areas.ProtectedArea;
import eu.matejkormuth.pexel.PexelCore.arenas.ArenaOption;
import eu.matejkormuth.pexel.PexelCore.arenas.MinigameArena;
import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;
import eu.matejkormuth.pexel.PexelCore.core.Region;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.matchmaking.GameState;
import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;

/**
 * Class used for /arena command.
 * 
 * @author Mato Kormuth
 * 
 */
public class ArenaCommand implements CommandExecutor {
    private final WorldEditPlugin we;
    
    public ArenaCommand() {
        this.we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin(
                "WorldEdit");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
            final String paramString, final String[] args) {
        if (command.getName().equalsIgnoreCase("arena")) {
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
    
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    private void processOpCommand(final Player sender, final String[] args) {
        if (args.length > 2) {
            String actionName = args[0].toLowerCase();
            String arenaName = args[1].toLowerCase();
            
            if (actionName.equalsIgnoreCase("create")) {
                if (args.length == 4) {
                    if (this.checkSelection(sender)) {
                        Region region = new Region(this.we.getSelection(sender));
                        String arenaType = args[2];
                        String minigameName = args[3];
                        //Check if minigame exists.
                        if (StorageEngine.getMinigame(minigameName) == null) {
                            sender.sendMessage(ChatManager.error("Minigame not defined: "
                                    + minigameName));
                            return;
                        }
                        
                        String className = null;
                        Class classType = null;
                        //Try to get by alias
                        if ((classType = StorageEngine.getByAlias(arenaType)) == null)
                            className = arenaType;
                        //Build object.
                        try {
                            Class c = null;
                            if (className != null)
                                c = Class.forName(className);
                            else
                                c = classType;
                            //Create instance
                            MinigameArena newArena = (MinigameArena) c.getDeclaredConstructor(
                                    Minigame.class, String.class, Region.class,
                                    int.class).newInstance(
                                    StorageEngine.getMinigame(minigameName), arenaName,
                                    region, 16);
                            sender.sendMessage(ChatManager.success("Created new arena with 16 slots."));
                            //Register arena to plugin.
                            Pexel.getMatchmaking().registerArena(newArena);
                            StorageEngine.addArena(newArena);
                        } catch (Exception e) {
                            e.printStackTrace();
                            sender.sendMessage(ChatManager.error("Create command failed: "
                                    + e.toString()));
                        }
                    }
                }
                else {
                    sender.sendMessage(ChatManager.error("/arena create <name> <arenaClass> <minigameClass>"));
                }
            }
            else if (actionName.equalsIgnoreCase("edit")) {
                String editAction = args[2];
                if (editAction.equalsIgnoreCase("gflag")) {
                    if (args.length >= 5) {
                        String flagName = args[3];
                        try {
                            Boolean flagValue = Boolean.parseBoolean(args[4]);
                            if (StorageEngine.getArena(arenaName) != null) {
                                StorageEngine.getArena(arenaName).setGlobalFlag(
                                        AreaFlag.valueOf(flagName), flagValue);
                                sender.sendMessage(ChatManager.success("Flag '"
                                        + flagName + "' set to '" + flagValue
                                        + "' in arena " + arenaName));
                            }
                            else {
                                throw new RuntimeException("Arena not found: "
                                        + arenaName);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(ChatManager.error("Edit command failed: "
                                    + ex.toString()));
                        }
                    }
                    else {
                        sender.sendMessage(ChatManager.error("/arena edit <name> gflag <flag> <value>"));
                    }
                }
                else if (editAction.equalsIgnoreCase("pflag")) {
                    if (args.length >= 5) {
                        String flagName = args[3];
                        String playerName = args[4];
                        try {
                            Boolean flagValue = Boolean.parseBoolean(args[5]);
                            UUID uuid = null;
                            if (Bukkit.getPlayerExact(playerName).isOnline()) {
                                uuid = Bukkit.getPlayerExact(playerName).getUniqueId();
                            }
                            else {
                                throw new RuntimeException("Play offline: " + playerName);
                            }
                            
                            if (StorageEngine.getArena(arenaName) != null) {
                                StorageEngine.getArena(arenaName).setPlayerFlag(
                                        AreaFlag.valueOf(flagName), flagValue, uuid);
                                sender.sendMessage(ChatManager.success("Flag '"
                                        + flagName + "' set to '" + flagValue
                                        + "' in arena " + arenaName + " for player "
                                        + playerName));
                            }
                            else {
                                throw new RuntimeException("Arena not found: "
                                        + arenaName);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(ChatManager.error("Edit command failed: "
                                    + ex.toString()));
                        }
                    }
                    else {
                        sender.sendMessage(ChatManager.error("/arena edit <name> pflag <flag> <value>"));
                    }
                }
                else if (editAction.equalsIgnoreCase("slots")) {
                    if (args.length >= 4) {
                        Integer slotCount = Integer.parseInt(args[3]);
                        try {
                            if (StorageEngine.getArena(arenaName) != null) {
                                StorageEngine.getArena(arenaName).setSlots(slotCount);
                                sender.sendMessage(ChatManager.success("Slots set to '"
                                        + slotCount + "' in arena " + arenaName));
                            }
                            else {
                                throw new RuntimeException("Arena not found: "
                                        + arenaName);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(ChatManager.error("Slots command failed: "
                                    + ex.toString()));
                        }
                    }
                    else {
                        sender.sendMessage(ChatManager.error("/arena edit <name> slots <count>"));
                    }
                }
                else if (editAction.equalsIgnoreCase("option")) {
                    if (args.length >= 5) {
                        String optionName = args[3];
                        String optionValue = args[4];
                        
                        try {
                            if (StorageEngine.getArena(arenaName) != null) {
                                boolean set = false;
                                MinigameArena arena = StorageEngine.getArena(arenaName);
                                
                                Field[] fields = arena.getClass().getDeclaredFields();
                                for (Field f : fields) {
                                    if (f.isAnnotationPresent(ArenaOption.class)) {
                                        if (f.getAnnotation(ArenaOption.class).name().equalsIgnoreCase(
                                                optionName)
                                                || f.getName().equalsIgnoreCase(
                                                        optionName)) {
                                            Class<?> type = f.getType();
                                            
                                            if (!f.isAccessible())
                                                f.setAccessible(true);
                                            
                                            if (type.equals(Integer.class)
                                                    || type.equals(int.class)) {
                                                f.set(arena,
                                                        Integer.parseInt(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Integer"));
                                                set = true;
                                            }
                                            else if (type.equals(Double.class)
                                                    || type.equals(double.class)) {
                                                f.set(arena,
                                                        Double.parseDouble(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Double"));
                                                set = true;
                                            }
                                            else if (type.equals(Float.class)
                                                    || type.equals(float.class)) {
                                                f.set(arena,
                                                        Float.parseFloat(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Float"));
                                                set = true;
                                            }
                                            else if (type.equals(Long.class)
                                                    || type.equals(long.class)) {
                                                f.set(arena, Long.parseLong(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Long"));
                                                set = true;
                                            }
                                            else if (type.equals(Short.class)
                                                    || type.equals(short.class)) {
                                                f.set(arena,
                                                        Short.parseShort(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Short"));
                                                set = true;
                                            }
                                            else if (type.equals(Byte.class)
                                                    || type.equals(byte.class)) {
                                                f.set(arena, Byte.parseByte(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Byte"));
                                                set = true;
                                            }
                                            else if (type.equals(Boolean.class)
                                                    || type.equals(boolean.class)) {
                                                f.set(arena,
                                                        Boolean.parseBoolean(optionValue));
                                                sender.sendMessage(ChatManager.success("Type: Boolean"));
                                                set = true;
                                            }
                                            else if (type.equals(String.class)) {
                                                f.set(arena, optionValue);
                                                sender.sendMessage(ChatManager.success("Type: String"));
                                                set = true;
                                            }
                                            else if (type.equals(Location.class)) {
                                                f.set(arena, sender.getLocation());
                                                sender.sendMessage(ChatManager.success("Type: Location. Taking your location as argument!"));
                                                set = true;
                                            }
                                            else {
                                                sender.sendMessage(ChatManager.error("I don't know how to set type '"
                                                        + type.getName() + "'!"));
                                                sender.sendMessage(ChatManager.error("If you believe, this is an error, create bug tickes at http://bugs.mertex.eu."));
                                            }
                                            
                                            break;
                                        }
                                    }
                                }
                                if (set)
                                    sender.sendMessage(ChatManager.success("Value of '"
                                            + optionName + "' set to '" + optionValue
                                            + "'!"));
                                else
                                    sender.sendMessage(ChatManager.error("Option probably not found! Contact minigame author to provide valid OPTION mapping!"));
                            }
                            else {
                                throw new RuntimeException("Arena not found: "
                                        + arenaName);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(ChatManager.error("Option command failed: "
                                    + ex.toString()));
                        }
                    }
                    else {
                        sender.sendMessage(ChatManager.error("/arena edit <name> option <optionName> <optionValue>"));
                    }
                }
                else if (editAction.equalsIgnoreCase("options")) {
                    try {
                        if (StorageEngine.getArena(arenaName) != null) {
                            sender.sendMessage(ChatColor.GOLD
                                    + "======= OPTIONS =======");
                            MinigameArena arena = StorageEngine.getArena(arenaName);
                            
                            for (Field f : arena.getClass().getDeclaredFields()) {
                                if (f.isAccessible())
                                    f.setAccessible(true);
                                
                                if (f.isAnnotationPresent(ArenaOption.class)) {
                                    if (Modifier.isFinal(f.getModifiers()))
                                        sender.sendMessage(ChatColor.RED + "[READONLY]"
                                                + ChatColor.YELLOW + f.getName()
                                                + ChatColor.WHITE + " = "
                                                + ChatColor.GREEN
                                                + f.get(arena).toString());
                                    else {
                                        if (f.get(arena) != null)
                                            sender.sendMessage(ChatColor.AQUA + "["
                                                    + f.getType().getSimpleName() + "] "
                                                    + ChatColor.GREEN + f.getName()
                                                    + ChatColor.WHITE + " = "
                                                    + ChatColor.GREEN
                                                    + f.get(arena).toString());
                                        else
                                            sender.sendMessage(ChatColor.AQUA + "["
                                                    + f.getType().getSimpleName() + "] "
                                                    + ChatColor.GREEN + f.getName()
                                                    + ChatColor.WHITE + " = "
                                                    + ChatColor.GREEN + "null");
                                        
                                    }
                                }
                            }
                        }
                        else {
                            throw new RuntimeException("Arena not found: " + arenaName);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        sender.sendMessage(ChatManager.error("Options command failed: "
                                + ex.toString()));
                    }
                }
                else if (editAction.equalsIgnoreCase("state")) {
                    if (args.length >= 4) {
                        String state = args[3];
                        GameState stateToSet;
                        try {
                            if (state.equalsIgnoreCase("open")) {
                                stateToSet = GameState.WAITING_EMPTY;
                            }
                            else if (state.equalsIgnoreCase("closed")) {
                                stateToSet = GameState.DISABLED;
                            }
                            else {
                                stateToSet = GameState.valueOf(state);
                            }
                            
                            if (StorageEngine.getArena(arenaName) != null) {
                                StorageEngine.getArena(arenaName).setState(stateToSet);
                                sender.sendMessage(ChatManager.success("State set to '"
                                        + stateToSet.toString() + "' in arena "
                                        + arenaName));
                            }
                            else {
                                throw new RuntimeException("Arena not found: "
                                        + arenaName);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(ChatManager.error("State command failed: "
                                    + ex.toString()));
                        }
                    }
                    else {
                        sender.sendMessage(ChatManager.error("/arena edit <name> state (open/closed)/(WAITING_EMPTY)"));
                    }
                }
                else {
                    sender.sendMessage(ChatManager.error("Unknown command!"));
                }
            }
            else if (actionName.equalsIgnoreCase("list")) {
                for (ProtectedArea a : StorageEngine.getAreas().values()) {
                    sender.sendMessage(ChatColor.YELLOW + "PA:" + a.getName());
                }
                
                for (MinigameArena a : StorageEngine.getArenas().values()) {
                    sender.sendMessage(ChatColor.GREEN + "MA:" + a.getName());
                }
            }
            else {
                sender.sendMessage(ChatManager.error("Valid subcommands are: create, edit, list"));
            }
        }
        else {
            sender.sendMessage(ChatManager.error("Wrong use!"));
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
