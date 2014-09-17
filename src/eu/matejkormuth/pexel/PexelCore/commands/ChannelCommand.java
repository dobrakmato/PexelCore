package eu.matejkormuth.pexel.PexelCore.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.chat.ChatChannel;
import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;
import eu.matejkormuth.pexel.PexelCore.chat.PlayerChannelSubscriber;
import eu.matejkormuth.pexel.PexelCore.chat.SubscribeMode;

/**
 * Class that handles execution of /channel commands.
 */
@CommandHandler(name = "channel")
public class ChannelCommand {
    
    @SubCommand(description = "Displays information about channels you are in.")
    public void main(final Player sender) {
        sender.sendMessage(ChatColor.GOLD + "Your channels: ");
        for (ChatChannel channel : ChatManager.getChannelsByPlayer(sender)) {
            sender.sendMessage(channel.getName());
        }
        sender.sendMessage(ChatColor.AQUA + "/channel help - Displays help information.");
    }
    
    @SubCommand(description = "Joins specified chat channel.")
    public void join(final Player sender, final String channelName) {
        ChatManager.getChannel(channelName).subscribe(
                new PlayerChannelSubscriber(sender, SubscribeMode.READ));
    }
    
    @SubCommand(description = "Lefts specified chat channel.")
    public void leave(final Player sender, final String channelName) {
        ChatManager.getChannel(channelName).unsubscribe(sender);
    }
}
