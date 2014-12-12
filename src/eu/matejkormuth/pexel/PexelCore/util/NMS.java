package eu.matejkormuth.pexel.PexelCore.util;

import net.minecraft.server.v1_8_R1.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

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
    public static final String VERSION    = Bukkit.getServer()
                                                  .getClass()
                                                  .getPackage()
                                                  .getName()
                                                  .replaceAll("org.bukkit.craftbukkit.",
                                                          "")
                                                  .replaceAll(".CraftServer", "");
    /**
     * Nazov balicku NMS.
     */
    public static final String PACKAGE_MC = "net.minecraft.server." + VERSION + ".";
    /**
     * Nazov balicku CB.
     */
    public static final String PACKAGE_CB = "org.bukkit.craftbukkit." + VERSION + ".";
    
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
