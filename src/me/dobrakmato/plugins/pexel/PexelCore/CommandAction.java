package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Basic sudo to player inventory action.
 * 
 * @author Mato Kormuth
 * 
 */
public class CommandAction implements Action
{
	private String	command	= "";
	
	/**
	 * Creates a new command action.
	 * 
	 * @param command
	 *            command of this action
	 */
	public CommandAction(final String command)
	{
		this.command = command;
	}
	
	@Override
	public void execute(final Player player)
	{
		player.performCommand(this.command.replace("%player%", player.getName()));
	}
	
	@Override
	public void load(final String string)
	{
		this.command = string;
	}
	
	@Override
	public String save()
	{
		return this.command;
	}
}
