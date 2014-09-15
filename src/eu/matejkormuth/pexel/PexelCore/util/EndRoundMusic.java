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
package eu.matejkormuth.pexel.PexelCore.util;

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
public class EndRoundMusic {
    private final List<String> musicTracks = new ArrayList<String>();
    
    private final Random       random      = new Random();
    
    private String randomMusic() {
        return this.musicTracks.get(this.random.nextInt(this.musicTracks.size()));
    }
    
    public void playMusic(final Player player) {
        String soundName = this.randomMusic();
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
                soundName, player.getLocation().getX(), player.getLocation().getY(),
                player.getLocation().getZ(), Float.MAX_VALUE, 1);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    
    public void playMusic(final Collection<Player> players) {
        String soundName = this.randomMusic();
        for (Player player : players) {
            PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
                    soundName, player.getLocation().getX(), player.getLocation().getY(),
                    player.getLocation().getZ(), Float.MAX_VALUE, 1);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
