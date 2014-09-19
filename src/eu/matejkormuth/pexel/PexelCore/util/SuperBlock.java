package eu.matejkormuth.pexel.PexelCore.util;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R3.block.CraftBlock;
import org.bukkit.entity.Entity;

/**
 * Block utils.
 */
public class SuperBlock extends CraftBlock {
    public SuperBlock(final CraftChunk chunk, final int x, final int y, final int z) {
        super(chunk, x, y, z);
    }
    
    public SuperBlock(final Block block) {
        super((CraftChunk) block.getChunk(), block.getLocation().getBlockX(),
                block.getLocation().getBlockY(), block.getLocation().getBlockZ());
    }
    
    public static final SuperBlock below(final Entity e) {
        return new SuperBlock(e.getWorld().getBlockAt(e.getLocation().getBlockX(),
                (int) Math.floor(e.getLocation().getY()), e.getLocation().getBlockZ()));
    }
}
