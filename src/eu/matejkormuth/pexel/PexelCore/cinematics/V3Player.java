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
package eu.matejkormuth.pexel.PexelCore.cinematics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.PacketPlayOutPosition;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaEntityDamage;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaEntityInventory;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaEntityRemove;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaEntitySpawn;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaEntityTeleport;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaEntityVelocity;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaFallingSand;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaParticleEffect;
import eu.matejkormuth.pexel.PexelCore.cinematics.v3meta.V3MetaSoundEffect;
import eu.matejkormuth.pexel.PexelCore.util.PacketHelper;
import eu.matejkormuth.pexel.PexelCore.util.SoundUtility;

/**
 * Prehravac V3 klipov.
 * 
 * @author Mato Kormuth
 * 
 */
public class V3Player {
    private final Player                  player;
    private final V3CameraClip            clip;
    private final Map<Long, LivingEntity> entityMapping   = Collections.synchronizedMap(new HashMap<Long, LivingEntity>());
    private boolean                       playing;
    private int                           currentFrameNum = 0;
    private V3CameraFrame                 currentFrame;
    private int                           syncUpdateTaskId;
    private final Queue<Runnable>         syncTasks       = new LinkedTransferQueue<Runnable>();
    private int                           syncTaskCounter = 0;
    private Runnable                      onCompleted;
    
    // private double lastX;
    // private double lastY;
    // private double lastZ;
    
    // private int eid = 0;
    
    public V3Player(final Player camera, final V3CameraClip clip) {
        this.player = camera;
        this.clip = clip;
    }
    
    public void play() {
        this.playing = true;
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                Integer.MAX_VALUE, 0));
        
        // Schedule sync updates.
        this.syncUpdateTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Pexel.getCore(), new Runnable() {
                    @Override
                    public void run() {
                        V3Player.this.syncUpdate();
                    }
                }, 0, 1);
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                V3Player.this.unsyncUpdate();
                // Cancel sync updates.
                Bukkit.getScheduler().cancelTask(V3Player.this.syncUpdateTaskId);
            }
        }).start();
    }
    
    /**
     * Volane 1000 / FPS krat za sekundu.
     */
    @SuppressWarnings("deprecation")
    private void unsyncUpdate() {
        while (this.playing) {
            // Get next frame.
            V3CameraFrame frame;
            
            if ((frame = this.nextFrame()) != null) {
                // Nastav kameru.
                
                // Hracovi posli paketu o teleportacii. Server aktualizuje
                // hracovu polohu v synUpdate, aby sa necrashoval.
                PacketHelper.send(this.player, new PacketPlayOutPosition(frame.camX,
                        frame.camY, frame.camZ, frame.yaw, frame.pitch,
                        com.google.common.collect.Sets.newLinkedHashSet()));
                
                // Spracuj zoom.
                
                // Spracuj meta.
                for (V3Meta meta : frame.getMetas()) {
                    switch (meta.getMetaType()) {
                        case V3MetaEntityDamage:
                            final V3MetaEntityDamage v3MetaEntityDamage = ((V3MetaEntityDamage) meta);
                            
                            //Bezpecne
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    V3Player.this.getEntity(
                                            v3MetaEntityDamage.getInternalId()).damage(
                                            v3MetaEntityDamage.getDamage());
                                }
                            });
                            break;
                        case V3MetaEntityInventory:
                            final V3MetaEntityInventory v3MetaEntityInventory = (V3MetaEntityInventory) meta;
                            
                            //Bezpecne.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    LivingEntity le = V3Player.this.getEntity(v3MetaEntityInventory.getInternalId());
                                    
                                    switch (v3MetaEntityInventory.getSlot()) {
                                        case 0:
                                            le.getEquipment()
                                                    .setBoots(
                                                            new ItemStack(
                                                                    v3MetaEntityInventory.getItemType(),
                                                                    1));
                                            break;
                                        case 1:
                                            le.getEquipment()
                                                    .setLeggings(
                                                            new ItemStack(
                                                                    v3MetaEntityInventory.getItemType(),
                                                                    1));
                                            break;
                                        case 2:
                                            le.getEquipment()
                                                    .setChestplate(
                                                            new ItemStack(
                                                                    v3MetaEntityInventory.getItemType(),
                                                                    1));
                                            break;
                                        case 3:
                                            le.getEquipment()
                                                    .setHelmet(
                                                            new ItemStack(
                                                                    v3MetaEntityInventory.getItemType(),
                                                                    1));
                                            break;
                                        case 4:
                                            le.getEquipment()
                                                    .setItemInHand(
                                                            new ItemStack(
                                                                    v3MetaEntityInventory.getItemType(),
                                                                    1));
                                            break;
                                    }
                                }
                            });
                            break;
                        case V3MetaEntityRemove:
                            final V3MetaEntityRemove v3MetaEntityRemove = (V3MetaEntityRemove) meta;
                            
                            //Bezpecne.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    V3Player.this.removeRemove(v3MetaEntityRemove.getInternalId());
                                }
                            });
                            break;
                        case V3MetaEntitySpawn:
                            final V3MetaEntitySpawn v3MetaEntitySpawn = (V3MetaEntitySpawn) meta;
                            
                            //Pre bezpocnost mudi byt synchronne.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    Entity e = V3Player.this.player.getWorld()
                                            .spawnEntity(
                                                    new Location(
                                                            V3Player.this.player.getWorld(),
                                                            v3MetaEntitySpawn.getPosX(),
                                                            v3MetaEntitySpawn.getPosY(),
                                                            v3MetaEntitySpawn.getPosZ(),
                                                            v3MetaEntitySpawn.getYaw(),
                                                            v3MetaEntitySpawn.getPitch()),
                                                    v3MetaEntitySpawn.getEntityType());
                                    if (e instanceof LivingEntity) {
                                        V3Player.this.addEntity(
                                                v3MetaEntitySpawn.getInternalId(),
                                                (LivingEntity) e);
                                        //NMS.makeIdiot((LivingEntity) e); //Osproti entitu.
                                    }
                                }
                            });
                            break;
                        case V3MetaEntityTeleport:
                            final V3MetaEntityTeleport v3MetaEntityTeleport = (V3MetaEntityTeleport) meta;
                            
                            //Musi byt synchronne pre bezpecnost.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    V3Player.this.getEntity(
                                            v3MetaEntityTeleport.getInternalId())
                                            .teleport(
                                                    new Location(
                                                            V3Player.this.player.getWorld(),
                                                            v3MetaEntityTeleport.getPosX(),
                                                            v3MetaEntityTeleport.getPosY(),
                                                            v3MetaEntityTeleport.getPosZ()));
                                }
                            });
                            
                            break;
                        case V3MetaEntityVelocity:
                            final V3MetaEntityVelocity v3MetaEntityVelocity = (V3MetaEntityVelocity) meta;
                            
                            //Musi byt synchronne pre bezpocnost.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    V3Player.this.getEntity(
                                            v3MetaEntityVelocity.getInternalId())
                                            .setVelocity(
                                                    new Vector(
                                                            v3MetaEntityVelocity.getVelX(),
                                                            v3MetaEntityVelocity.getVelY(),
                                                            v3MetaEntityVelocity.getVelZ()));
                                }
                            });
                            ;
                            break;
                        case V3MetaFallingSand:
                            final V3MetaFallingSand v3MetaFallingSand = (V3MetaFallingSand) meta;
                            
                            //Packet rewrite
                            
                            /*
                             * [13:34:41] [Netty IO #5/DEBUG]: OUT: [PLAY:14]
                             * net.minecraft.server.v1_7_R3.PacketPlayOutSpawnEntity[id=3804, type=70, x=9,50, y=64,50,
                             * z=549,50] [13:34:41] [Netty IO #5/DEBUG]: OUT: [PLAY:28]
                             * net.minecraft.server.v1_7_R3.PacketPlayOutEntityMetadata[] [13:34:41] [Netty IO
                             * #5/DEBUG]: OUT: [PLAY:18]
                             * net.minecraft.server.v1_7_R3.PacketPlayOutEntityVelocity[id=3804, x=0,00, y=0,00, z=0,00]
                             * [13:34:41] [Netty IO #5/DEBUG]: OUT: [PLAY:25]
                             * net.minecraft.server.v1_7_R3.PacketPlayOutEntityHeadRotation[id=3804, rot=0]
                             */
                            
                            /*
                             * PacketHelper.send( this.player, ((PacketPlayOutSpawnEntity) new
                             * V3PacketPlayOutSpawnEntity( Mertexfun.random.nextInt(), v3MetaFallingSand.getPosX(),
                             * v3MetaFallingSand.getPosY(), v3MetaFallingSand.getPosZ(), 0, 0, 70,
                             * v3MetaFallingSand.getMaterial().getId() | (0 << 0x10), v3MetaFallingSand.getVelX(),
                             * v3MetaFallingSand.getVelY(), v3MetaFallingSand.getVelZ())));
                             */
                            
                            //Musi byt pre bezpocnost synchronne.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    FallingBlock fb = V3Player.this.player.getWorld()
                                            .spawnFallingBlock(
                                                    new Location(
                                                            V3Player.this.player.getWorld(),
                                                            v3MetaFallingSand.getPosX(),
                                                            v3MetaFallingSand.getPosY(),
                                                            v3MetaFallingSand.getPosZ()),
                                                    v3MetaFallingSand.getMaterial(),
                                                    (byte) 0);
                                    fb.setVelocity(new Vector(
                                            v3MetaFallingSand.getVelX(),
                                            v3MetaFallingSand.getVelY(),
                                            v3MetaFallingSand.getVelZ()));
                                    fb.setDropItem(false);
                                    fb.setMetadata(
                                            "isV3",
                                            new FixedMetadataValue(Pexel.getCore(), true));
                                }
                            });
                            
                            break;
                        case V3MetaParticleEffect:
                            V3MetaParticleEffect v3MetaParticleEffect = (V3MetaParticleEffect) meta;
                            
                            /*
                             * ParticleEffect2.fromId(v3MetaParticleEffect.getParticle()) .display( new
                             * Location(this.player.getWorld(), v3MetaParticleEffect.getPosX(),
                             * v3MetaParticleEffect.getPosY(), v3MetaParticleEffect.getPosZ()),
                             * v3MetaParticleEffect.getOffsetX(), v3MetaParticleEffect.getOffsetY(),
                             * v3MetaParticleEffect.getOffsetZ(), v3MetaParticleEffect.getSpeed(),
                             * v3MetaParticleEffect.getAmount());
                             */
                            break;
                        case V3MetaSoundEffect:
                            V3MetaSoundEffect v3MetaSoundEffect = (V3MetaSoundEffect) meta;
                            
                            SoundUtility.playCustomSound(this.player,
                                    v3MetaSoundEffect.getName(),
                                    v3MetaSoundEffect.getVolume(),
                                    v3MetaSoundEffect.getPitch());
                            break;
                        case V3MetaExplosion:
                            
                            break;
                        case V3MetaEntityMove:
                            //final V3MetaEntityMove v3MetaEntityMove = (V3MetaEntityMove) meta;
                            
                            //PacketHelper.send(
                            //        this.player,
                            //       new PacketPlayOutRelEntityMove(
                            //               V3Player.this.getEntity(
                            ///                        v3MetaEntityMove.getInternalId())
                            //                       .getEntityId(),
                            //               NMS.fixedPointNumByte(v3MetaEntityMove.getMovX()),
                            //               NMS.fixedPointNumByte(v3MetaEntityMove.getMovY()),
                            //               NMS.fixedPointNumByte(v3MetaEntityMove.getMovZ())));
                            
                            /*
                             * NMS.relMoveEntity( V3Player.this.getEntity(v3MetaEntityMove.getInternalId()),
                             * v3MetaEntityMove.getMovX(), v3MetaEntityMove.getMovY(), v3MetaEntityMove.getMovZ());
                             */
                            
                            //Synchronizovane.
                            this.syncTasks.add(new Runnable() {
                                @Override
                                public void run() {
                                    
                                }
                            });
                            break;
                        default:
                            break;
                    
                    }
                }
            }
            else {
                this.playing = false;
                
                // Remove things, clean up.
                synchronized (this.entityMapping) {
                    // Kill all left entites.
                    for (LivingEntity e : this.entityMapping.values()) {
                        e.damage(400D);
                    }
                }
                
                this.player.setFlying(false);
                if (this.player.getGameMode() == GameMode.SURVIVAL)
                    this.player.setAllowFlight(false);
                this.player.removePotionEffect(PotionEffectType.INVISIBILITY);
                //Zavolaj to co treba.
                if (this.onCompleted != null)
                    this.onCompleted.run();
            }
            
            // Spinkaj.
            try {
                Thread.sleep(1000 / this.clip.FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * @param internalId
     */
    private void removeRemove(final long internalId) {
        synchronized (this.entityMapping) {
            this.getEntity(internalId).damage(500D);
            this.entityMapping.remove(internalId);
        }
    }
    
    /**
     * @param internalId
     * @param e
     */
    private void addEntity(final long internalId, final LivingEntity e) {
        synchronized (this.entityMapping) {
            this.entityMapping.put(internalId, e);
        }
    }
    
    /**
     * Vrati entitu podla internalID.
     * 
     * @param internalId
     * @return
     */
    private LivingEntity getEntity(final long internalId) {
        return this.entityMapping.get(internalId);
    }
    
    /**
     * Vrati dalsi frame.
     * 
     * @return
     */
    private V3CameraFrame nextFrame() {
        this.currentFrameNum++;
        if (this.currentFrameNum < this.clip.frames.size() + 1) {
            return (this.currentFrame = this.clip.frames.get(this.currentFrameNum - 1));
        }
        else {
            return null;
        }
    }
    
    /**
     * Volane 20 krat za sekundu.
     */
    public void syncUpdate() {
        if (this.playing) {
            //Pomalicky zachovovaj poziciu hraca aj na serveri.
            EntityPlayer ep = ((CraftPlayer) this.player).getHandle();
            ep.locX = this.currentFrame.camX;
            ep.locY = this.currentFrame.camY;
            ep.locZ = this.currentFrame.camZ;
            //Spracuj ulohy, co sa musia spravit synchronizovane.
            this.syncTaskCounter = 0;
            while (this.syncTaskCounter < Short.MAX_VALUE
                    && this.syncTasks.peek() != null) {
                this.syncTasks.poll().run();
                this.syncTaskCounter++;
            }
        }
    }
    
    /**
     * @param runnable
     */
    public void setOnCompleted(final Runnable runnable) {
        this.onCompleted = runnable;
    }
}
