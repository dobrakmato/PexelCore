/**
 * SoundEffects.java part of mertexfun. by M
 * 
 * (C) M 26.3.2014 19:17:35
 */
package eu.matejkormuth.pexel.PexelCore.util;

import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Trieda, ktora spracovava prehravanie vlastnych zvukovych efektov.
 * 
 * @author Matej Kormuth
 * 
 */
public class SoundUtility {
    //Zvuky.
    public static final String MUSIC_LOBBY = "music_lobby";
    
    /**
     * Prehra vlastsny zvuk podla mena.
     * 
     * @param p
     * @param soundName
     * @param volume
     * @param pitch
     * @param worldwide
     */
    public static void playCustomSound(final Player p, final String soundName,
            final float volume, final float pitch, final boolean worldwide) {
        System.out.println("CustomSound: " + p.getName() + ", " + soundName + ", "
                + volume + ", " + pitch + ", " + worldwide);
        if (worldwide) {
            PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
                    soundName, p.getLocation().getX(), p.getLocation().getY(),
                    p.getLocation().getZ(), Float.MAX_VALUE, pitch);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
        else {
            PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
                    soundName, p.getLocation().getX(), p.getLocation().getY(),
                    p.getLocation().getZ(), volume, pitch);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    
    /**
     * Prehra vlastny zvuk vsetkym hracom v okoli
     * 
     * @param player_loc
     * @param soundName
     * @param volume
     * @param pitch
     */
    public static void playCustomSound(final Player player_loc, final String soundName,
            final float volume, final float pitch) {
        PacketPlayOutNamedSoundEffect packet2 = new PacketPlayOutNamedSoundEffect(
                soundName, player_loc.getLocation().getX(), player_loc.getLocation()
                        .getY(), player_loc.getLocation().getZ(), volume, pitch);
        ((CraftPlayer) player_loc).getHandle().playerConnection.sendPacket(packet2);
        
        for (Entity e : player_loc.getNearbyEntities(100, 100, 100)) {
            if (e instanceof Player) {
                if (player_loc.getLocation().distanceSquared(e.getLocation()) < 10000) {
                    PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
                            soundName, e.getLocation().getX(), e.getLocation().getY(),
                            e.getLocation().getZ(), 1
                                    - (float) (player_loc.getLocation().distanceSquared(
                                            e.getLocation()) / 10000) * volume, pitch);
                    ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
    }
    
    /**
     * @param bj
     * @param soundName
     * @param volume
     * @param pitch
     */
    public static void playCustomSound(final Entity bj, final String soundName,
            final float volume, final float pitch) {
        for (Entity e : bj.getNearbyEntities(100, 100, 100)) {
            if (e instanceof Player) {
                if (bj.getLocation().distanceSquared(e.getLocation()) < 10000) {
                    PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
                            soundName, e.getLocation().getX(), e.getLocation().getY(),
                            e.getLocation().getZ(), 1
                                    - (float) (bj.getLocation().distanceSquared(
                                            e.getLocation()) / 10000) * volume, pitch);
                    ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
    }
}
