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
    private final Map<String, Map<String, Method>> subcommands = new HashMap<String, Map<String, Method>>();
    private final Map<String, Class<?>>            commands    = new HashMap<String, Class<?>>();
    
    public CommandManager() {
        
    }
    
    public void registerCommands(final Object command) {
        Log.info("Register command on object: " + command.getClass().getSimpleName()
                + "#" + command.hashCode());
        Class<?> clazz = command.getClass();
        if (clazz.isAnnotationPresent(Command.class)) {
            this.registerCommand(clazz);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(SubCommand.class)
                        || method.getName().equalsIgnoreCase("main")) {
                    this.registerSubcommand(clazz, method);
                }
            }
        }
        else {
            throw new AnnotationNotPresentException("Annotation: Command; Class: clazz");
        }
    }
    
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
                        //Executing subcommand
                        if (parts.length == 2) {
                            this.invoke(this.commands.get(baseCommand),
                                    this.subcommands.get(baseCommand).get(subCommand),
                                    sender);
                        }
                        else {
                            Object[] args = new String[parts.length - 2];
                            System.arraycopy(parts, 2, args, 0, parts.length - 2);
                            this.invoke(this.commands.get(baseCommand),
                                    this.subcommands.get(baseCommand).get(subCommand),
                                    sender, args);
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
                                        + ChatColor.GREEN
                                        
                                        + " - " + annotation.description());
                            }
                        }
                        else {
                            Object[] args = new String[parts.length - 1];
                            System.arraycopy(parts, 1, args, 0, parts.length - 1);
                            this.invoke(this.commands.get(baseCommand),
                                    this.subcommands.get(baseCommand).get("main"),
                                    sender, args);
                        }
                    }
                }
            }
        }
        else {
            sender.sendMessage(ChatManager.error("Unknown command!"));
        }
    }
    
    private boolean hasPermission(final Player sender, final String baseCommand) {
        return true;
    }
    
    private void invoke(final Object command, final Method subcommand,
            final Player invoker, final Object... args) {
        try {
            
            subcommand.invoke(command, invoker, args);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            invoker.sendMessage(ChatManager.error("Internal server error occured while attempting to execute this command!"));
        }
    }
    
    private void registerSubcommand(final Class<?> clazz, final Method method) {
        Log.info("  Register subcommand: "
                + clazz.getAnnotation(Command.class).name().toLowerCase() + " -> "
                + method.getAnnotation(SubCommand.class).name().toLowerCase());
        
        if (!method.isAccessible())
            method.setAccessible(true);
        
        if (!method.getAnnotation(SubCommand.class).name().equals(""))
            this.subcommands.get(clazz.getAnnotation(Command.class).name().toLowerCase()).put(
                    method.getAnnotation(SubCommand.class).name().toLowerCase(), method);
        else
            this.subcommands.get(clazz.getAnnotation(Command.class).name().toLowerCase()).put(
                    method.getName().toLowerCase(), method);
    }
    
    private void registerCommand(final Class<?> clazz) {
        Log.info(" Register command: "
                + clazz.getAnnotation(Command.class).name().toLowerCase());
        this.commands.put(clazz.getAnnotation(Command.class).name().toLowerCase(), clazz);
        this.subcommands.put(clazz.getAnnotation(Command.class).name().toLowerCase(),
                new HashMap<String, Method>());
    }
}
