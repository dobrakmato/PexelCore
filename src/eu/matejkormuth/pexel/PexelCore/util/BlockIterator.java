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
    private int            directionX;
    private int            directionY;
    private int            directionZ;
    
    /**
     * Creates new instance of <b>BlockIterator</b>. This class should be used for iterating over blocks in specified
     * direction and should not be used to detect block patterns.
     * 
     * @param startBlock
     * @param direction
     */
    public BlockIterator(final Block startBlock, final BlockFace direction) {
        this.location = startBlock.getLocation();
        this.directionX = direction.getModX();
        this.directionY = direction.getModY();
        this.directionZ = direction.getModZ();
    }
    
    public BlockIterator(final Block startBlock, final int modX, final int modY,
            final int modZ) {
        this.location = startBlock.getLocation();
        this.directionX = modX;
        this.directionY = modY;
        this.directionZ = modZ;
    }
    
    @Override
    public boolean hasNext() {
        return true;
    }
    
    @Override
    public Block next() {
        return this.location.add(this.directionX, this.directionY, this.directionZ).getBlock();
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
     * @param direction
     *            the direction to set
     */
    public void setDirection(final BlockFace direction) {
        this.directionX = direction.getModX();
        this.directionY = direction.getModY();
        this.directionZ = direction.getModZ();
    }
    
    public void setDirection(final int modX, final int modY, final int modZ) {
        this.directionX = modX;
        this.directionY = modY;
        this.directionZ = modZ;
    }
}
