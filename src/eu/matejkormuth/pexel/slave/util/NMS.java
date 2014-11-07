package eu.matejkormuth.pexel.slave.util;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R3.Entity;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityLiving;
import net.minecraft.server.v1_7_R3.NBTTagCompound;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Helper na pracu s NMS kodom.
 * 
 * @author Matej Kormuth
 * 
 */
public class NMS {
    /**
     * Verzia serveru.
     */
    public static final String VERSION    = Bukkit.getServer().getClass().getPackage().getName().replaceAll(
                                                  "org.bukkit.craftbukkit.", "").replaceAll(
                                                  ".CraftServer", "");
    /**
     * Nazov balicku NMS.
     */
    public static final String PACKAGE_MC = "net.minecraft.server." + VERSION + ".";
    /**
     * Nazov balicku CB.
     */
    public static final String PACKAGE_CB = "org.bukkit.craftbukkit." + VERSION + ".";
    
    /**
     * Unwraps CraftItemStack, and returns NMS ItemStack.
     * 
     * @return
     */
    public static net.minecraft.server.v1_7_R3.ItemStack getNMSItemStack(
            final ItemStack itemstack) {
        if (itemstack == null)
            throw new NullArgumentException("itemstack");
        CraftItemStack cbStack = ((CraftItemStack) itemstack);
        try {
            Field handleField = cbStack.getClass().getDeclaredField("handle");
            handleField.setAccessible(true);
            return (net.minecraft.server.v1_7_R3.ItemStack) handleField.get(cbStack);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    /**
     * Vrati display name specifikovaneho item stacku.
     * 
     * @param itemstack
     *            item stack
     * @return display name
     */
    public static String getItemDisplayName(final ItemStack itemstack) {
        // Ziskaj NMS ItemStack
        net.minecraft.server.v1_7_R3.ItemStack nmsstack = NMS.getNMSItemStack(itemstack);
        // Ziskaj tag, ktory potrebujeme.
        return ((NBTTagCompound) nmsstack.tag.get("display")).getString("Name");
    }
    
    /**
     * Vrati, ci ItemStack ma NBT tag s menom 'display', teda ci ma cusotmname alebo lore.
     * 
     * @param itemstack
     * @return
     */
    public static boolean hasDisplayTag(final ItemStack itemstack) {
        // Ziskaj NMS ItemStack
        net.minecraft.server.v1_7_R3.ItemStack nmsstack = NMS.getNMSItemStack(itemstack);
        // Zisti ci existuje tag 'display'
        if (nmsstack.tag != null)
            return nmsstack.tag.hasKey("display");
        else
            return false;
    }
    
    /**
     * Nastavi display name specifikovaneho item stacku.
     * 
     * @param itemstack
     *            item stack
     * @param name
     *            display name
     */
    public static void setItemDisplayName(final ItemStack itemstack, final String name) {
        // Ziskaj NMS ItemStack
        net.minecraft.server.v1_7_R3.ItemStack nmsstack = NMS.getNMSItemStack(itemstack);
        // Ziskaj tag, ktory potrebujeme.
        ((NBTTagCompound) nmsstack.tag.get("display")).setString("Name", name);
    }
    
    public static void setHeadYaw(final Entity en, float yaw) {
        if (!(en instanceof EntityLiving))
            return;
        EntityLiving handle = (EntityLiving) en;
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }
        
        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }
        handle.aO = yaw;
        if (!(handle instanceof EntityHuman))
            handle.aM = yaw;
        handle.aP = yaw;
    }
    
    /**
     * @param entity
     * @param movX
     * @param movY
     * @param movZ
     */
    public static void relMoveEntity(final LivingEntity entity, final double movX,
            final double movY, final double movZ) {
        if (entity instanceof EntityLiving) {
            ((EntityLiving) entity).move(movX, movY, movZ);
        }
    }
    
    /**
     * @param movX
     * @return
     */
    public static int fixedPointNumInteger(final double i) {
        if (i >= 134217728)
            System.out.println("Can't cast double bigger then 134217728 to fixed point number using int32.");
        return (int) (i * 32);
    }
    
    /**
     * @param i
     * @return
     */
    public static byte fixedPointNumByte(final double i) {
        if (i >= 8)
            System.out.println("Can't cast double bigger then 8 to fixed point number using byte.");
        return (byte) (i * 32);
    }
    
    /**
     * Osporti entitu, ak je entita EntityInsentient tak, ze jej zmaze goalSelector golas a targetSelector goals, cize
     * AI.
     * 
     * FIXME!!!!!
     * 
     * @param e
     */
    public static void makeIdiot(final org.bukkit.entity.LivingEntity e) {
        //EntityInsentient ei = (EntityInsentient) e;
        /*
         * try { Field gsa = net.minecraft.server.v1_7_R3.PathfinderGoalSelector.class.getDeclaredField("c");
         * gsa.setAccessible(true); gsa.set(this.goalSelector, new UnsafeList()); gsa.set(this.targetSelector, new
         * UnsafeList());
         * 
         * Field gsb = net.minecraft.server.v1_7_R3.PathfinderGoalSelector.class.getDeclaredField("b");
         * gsb.setAccessible(true); gsb.set(this.goalSelector, new UnsafeList()); gsb.set(this.targetSelector, new
         * UnsafeList());
         * 
         * } catch (SecurityException e) { e.printStackTrace(); } catch (NoSuchFieldException e) { e.printStackTrace();
         * } catch (IllegalArgumentException e) { e.printStackTrace(); } catch (IllegalAccessException e) {
         * e.printStackTrace(); }
         */
        
    }
}
