package eu.matejkormuth.pexel.PexelCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.bans.BanBase;
import eu.matejkormuth.pexel.PexelCore.bans.BanUtils;
import eu.matejkormuth.pexel.PexelCore.bans.NamedBanAuthor;
import eu.matejkormuth.pexel.PexelNetworking.Server;

@CommandHandler(name = "bans")
public class BansCommand {
    @SubCommand(description = "main command, does nothing")
    public void main(final Player sender) {
        
    }
    
    @SuppressWarnings("deprecation")
    @SubCommand(description = "Adds ban")
    public void add(final Player sender, final String playerName, final String... reason) {
        BanBase ban = new BanBase(Strings.join(reason, " "), new NamedBanAuthor(
                sender.getName()), Bukkit.getPlayer(playerName), Server.THIS_SERVER);
        Pexel.getBans().addBan(ban);
        Bukkit.getPlayer(playerName).kickPlayer(BanUtils.formatBannedMessage(ban));
        sender.sendMessage("You have banned player " + playerName + " permanently for "
                + Strings.join(reason, " "));
    }
    
    @SuppressWarnings("deprecation")
    @SubCommand(description = "Adds ban")
    public void add(final Player sender, final String playerName, final String lenght,
            final String... reason) {
        BanBase ban = new BanBase(Integer.parseInt(lenght), Strings.join(reason, " "),
                new NamedBanAuthor(sender.getName()), Bukkit.getPlayer(playerName),
                Server.THIS_SERVER);
        Pexel.getBans().addBan(ban);
        Bukkit.getPlayer(playerName).kickPlayer(BanUtils.formatBannedMessage(ban));
        sender.sendMessage("You have banned player " + playerName + " temporarely for "
                + Strings.join(reason, " "));
    }
    
    @SubCommand(description = "Removes ban")
    public void remove(final Player sender, final String playerName) {
        sender.sendMessage("You cannot unban players at this moment. Please wait for developer, to implment unbans.");
    }
}
