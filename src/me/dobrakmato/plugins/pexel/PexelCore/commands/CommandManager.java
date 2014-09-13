package me.dobrakmato.plugins.pexel.PexelCore.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.chat.ChatManager;
import me.dobrakmato.plugins.pexel.PexelCore.core.Log;
import me.dobrakmato.util.AnnotationNotPresentException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class that is used for dynamic command registration.
 */
public class CommandManager {
    /**
     * Map of subcommands.
     */
    private final Map<String, Map<String, Method>> subcommands = new HashMap<String, Map<String, Method>>();
    /**
     * Map of commands.
     */
    private final Map<String, Class<?>>            commands    = new HashMap<String, Class<?>>();
    /**
     * Map of command aliases.
     */
    private final Map<String, String>              aliases     = new HashMap<String, String>();
    
    public CommandManager() {
        
    }
    
    /**
     * Tries to register specified object as command handler.
     * 
     * @param command
     *            command handler
     */
    public void registerCommands(final Object command) {
        Log.info("Register command on object: " + command.getClass().getSimpleName()
                + "#" + command.hashCode());
        Class<?> clazz = command.getClass();
        if (clazz.isAnnotationPresent(CommandHandler.class)) {
            this.registerCommand(clazz);
            this.registerAliases(clazz);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(SubCommand.class)) {
                    this.registerSubcommand(clazz, method);
                }
            }
        }
        else {
            throw new AnnotationNotPresentException("Annotation: Command; Class: clazz");
        }
    }
    
    /**
     * Parses command from string and tries to execute it as specified player.
     * 
     * @param sender
     *            executor
     * @param command
     *            command
     */
    public void parseCommand(final Player sender, final String command) {
        String parts[] = command.split("\\s+");
        String baseCommand = parts[0];
        
        if (this.commands.containsKey(baseCommand.toLowerCase())) {
            if (this.hasPermission(sender, baseCommand)) {
                //If no subcommand
                if (parts.length == 1) {
                    this.invoke(this.commands.get(baseCommand),
                            this.subcommands.get(baseCommand).get("main"), sender);
                }
                else {
                    String subCommand = parts[1];
                    if (this.subcommands.get(baseCommand).containsKey(subCommand)) {
                        if (this.hasPermission(sender, baseCommand + "." + subCommand)) {
                            //Executing subcommand
                            if (parts.length == 2) {
                                this.invoke(
                                        this.commands.get(baseCommand),
                                        this.subcommands.get(baseCommand).get(subCommand),
                                        sender);
                            }
                            else {
                                Object[] args = new String[parts.length - 2];
                                System.arraycopy(parts, 2, args, 0, parts.length - 2);
                                this.invoke(
                                        this.commands.get(baseCommand),
                                        this.subcommands.get(baseCommand).get(subCommand),
                                        sender, args);
                            }
                        }
                        else {
                            sender.sendMessage(ChatManager.error("You don't have permission!"));
                        }
                    }
                    else {
                        if (subCommand.equalsIgnoreCase("help")) {
                            sender.sendMessage(ChatColor.YELLOW + "Command help: "
                                    + baseCommand);
                            for (Method m : this.subcommands.get(baseCommand).values()) {
                                SubCommand annotation = m.getAnnotation(SubCommand.class);
                                sender.sendMessage(ChatColor.BLUE + "/" + baseCommand
                                        + ChatColor.RED + " " + annotation.name()
                                        + ChatColor.GOLD + " <[args...]>"
                                        + ChatColor.GREEN + " - "
                                        + annotation.description());
                            }
                        }
                        else {
                            if (this.hasPermission(sender, baseCommand + "."
                                    + subCommand)) {
                                Object[] args = new String[parts.length - 1];
                                System.arraycopy(parts, 1, args, 0, parts.length - 1);
                                this.invoke(this.commands.get(baseCommand),
                                        this.subcommands.get(baseCommand).get("main"),
                                        sender, args);
                            }
                            else {
                                sender.sendMessage(ChatManager.error("You don't have permission!"));
                            }
                        }
                    }
                }
            }
            else {
                sender.sendMessage(ChatManager.error("You don't have permission!"));
            }
        }
        else {
            sender.sendMessage(ChatManager.error("Unknown command!"));
        }
    }
    
    /**
     * Returns whether specified player has permission to specififed command.
     * 
     * @param sender
     *            player
     * @param baseCommand
     *            command
     * @return true or false
     */
    private boolean hasPermission(final Player sender, final String node) {
        return true;
    }
    
    /**
     * Invokes specified subcommand of command on specififed player weith specified arguments.
     * 
     * @param command
     *            command
     * @param subcommand
     *            sub command
     * @param invoker
     *            player
     * @param args
     *            args
     */
    private void invoke(final Object command, final Method subcommand,
            final Player invoker, final Object... args) {
        try {
            String argsString = "[";
            for (Object o : args)
                argsString += o.toString() + ",";
            Log.info("Invoking command "
                    + command.getClass().getAnnotation(CommandHandler.class).name()
                    + " -> " + subcommand.getAnnotation(SubCommand.class).name()
                    + " on player " + invoker.getName() + " with args: " + argsString
                    + "]");
            subcommand.invoke(command, invoker, args);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            invoker.sendMessage(ChatManager.error("Internal server error occured while attempting to execute this command!"));
        }
    }
    
    private void registerSubcommand(final Class<?> clazz, final Method method) {
        String baseCommand = clazz.getAnnotation(CommandHandler.class).name().toLowerCase();
        String subCommand = method.getName().toLowerCase();
        
        if (!method.getAnnotation(SubCommand.class).name().equalsIgnoreCase(""))
            subCommand = method.getAnnotation(SubCommand.class).name().toLowerCase();
        
        Log.info("  Register subcommand: " + baseCommand + " -> " + subCommand);
        
        if (!method.isAccessible())
            method.setAccessible(true);
        
        this.subcommands.get(baseCommand).put(subCommand, method);
    }
    
    private void registerCommand(final Class<?> clazz) {
        String baseCommand = clazz.getAnnotation(CommandHandler.class).name().toLowerCase();
        
        Log.info(" Register command: " + baseCommand);
        this.commands.put(baseCommand, clazz);
        this.subcommands.put(baseCommand, new HashMap<String, Method>());
    }
    
    private void registerAliases(final Class<?> clazz) {
        String baseName = clazz.getAnnotation(CommandHandler.class).name();
        for (String alias : clazz.getAnnotation(CommandHandler.class).aliases()) {
            this.aliases.put(baseName, alias);
        }
    }
}
