package eu.matejkormuth.pexel.PexelCore.util;

import org.bukkit.Location;
import org.bukkit.material.MaterialData;

/**
 * Class used for detecting block patterns.
 */
public class BlockPattern {
    private final MaterialData[][][] pattern;
    private int                      anchorX;
    private int                      anchorY;
    private int                      anchorZ;
    
    /**
     * Creates new instance of BlockPattern with specified MaterialData pattern in x, y, z coordinate order.
     * 
     * @param pattern
     *            pattern of material data in order [x][y][z]
     */
    public BlockPattern(final MaterialData[][][] pattern) {
        this.pattern = pattern;
    }
    
    /**
     * Sets relative locaton of anchor block.
     * 
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     * @param z
     *            z-coordinate
     */
    public void setAnchor(final int x, final int y, final int z) {
        this.anchorX = x;
        this.anchorY = y;
        this.anchorZ = z;
    }
    
    /**
     * Checks for match on specified lcoation.
     * 
     * @param location
     *            location of nachor block
     * @return <b>true</b> if match found, <b>false</b> otherwise
     */
    public boolean match(final Location location) {
        for (int x = 0; x < this.pattern.length; x++) {
            for (int y = 0; y < this.pattern[0].length; y++) {
                for (int z = 0; z < this.pattern[0][0].length; z++) {
                    int absX = location.getBlockX() - this.anchorX + x;
                    int absY = location.getBlockY() - this.anchorY + y;
                    int absZ = location.getBlockZ() - this.anchorZ + z;
                    
                    if (this.pattern[x][y][z] != null) {
                        if (location.getWorld().getBlockAt(absX, absY, absZ) != this.pattern[x][y][z]) { return false; }
                    }
                }
            }
        }
        return true;
    }
}
