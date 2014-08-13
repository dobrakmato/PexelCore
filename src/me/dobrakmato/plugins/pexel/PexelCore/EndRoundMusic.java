package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R3.PacketPlayOutNamedSoundEffect;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Class used for playing endround music.
 * 
 * @author Mato Kormuth
 * 
 */
public class EndRoundMusic
{
	private final List<String>	musicTracks	= new ArrayList<String>();
	
	private final Random		random		= new Random();
	
	private String randomMusic()
	{
		return this.musicTracks.get(this.random.nextInt(this.musicTracks.size()));
	}
	
	public void playMusic(final Player player)
	{
		String soundName = this.randomMusic();
		PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
				soundName, player.getLocation().getX(),
				player.getLocation().getY(), player.getLocation().getZ(),
				Float.MAX_VALUE, 1);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void playMusic(final Collection<Player> players)
	{
		String soundName = this.randomMusic();
		for (Player player : players)
		{
			PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
					soundName, player.getLocation().getX(),
					player.getLocation().getY(), player.getLocation().getZ(),
					Float.MAX_VALUE, 1);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
