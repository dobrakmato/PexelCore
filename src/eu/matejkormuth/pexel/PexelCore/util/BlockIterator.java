package eu.matejkormuth.pexel.PexelCore.util;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Block iterator.
 */
public class BlockIterator implements Iterator<Block> {
    private final Location location;
    private BlockFace      direction;
    
    public BlockIterator(final Block startBlock, final BlockFace direction) {
        this.location = startBlock.getLocation();
        this.direction = direction;
    }
    
    @Override
    public boolean hasNext() {
        return true;
    }
    
    @Override
    public Block next() {
        return this.location.add(this.direction.getModX(), this.direction.getModY(),
                this.direction.getModZ()).getBlock();
    }
    
    /**
     * Iterates by specified direction until specified material is detected. Then it returns first block, that is from
     * specififed material.
     * 
     * @param specified
     *            specified material
     * @return first block in specified direction of specified type
     */
    public Block until(final Material specified) {
        do {
            return this.location.getBlock();
        } while (this.next().getType() == specified);
    }
    
    @Override
    public void remove() {
        this.location.getBlock().setType(Material.AIR);
    }
    
    /**
     * @return the direction
     */
    public BlockFace getDirection() {
        return this.direction;
    }
    
    /**
     * @param direction
     *            the direction to set
     */
    public void setDirection(final BlockFace direction) {
        this.direction = direction;
    }
}
