package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

/**
 * Class that stores information about change in blocks.
 * 
 * @author Mato Kormuth
 * 
 */
public class BlockChange
{
	//Values before change.
	private final Material		oldMaterial;
	private final MaterialData	oldMaterialData;
	//Location.
	private final Location		blockLocation;
	
	/**
	 * Creates a new block change object.
	 * 
	 * @param oldMaterial
	 *            old material
	 * @param oldData
	 *            old data
	 * @param blockLocation
	 *            location of block.
	 */
	public BlockChange(final Material oldMaterial,
			final MaterialData oldMaterialData, final Location blockLocation)
	{
		this.oldMaterial = oldMaterial;
		this.oldMaterialData = oldMaterialData;
		this.blockLocation = blockLocation;
	}
	
	/**
	 * Creates a new block change from Block.
	 * 
	 * @param block
	 *            the state of block before change
	 */
	public BlockChange(final Block block)
	{
		this.oldMaterial = block.getType();
		this.oldMaterialData = block.getState().getData();
		this.blockLocation = block.getLocation();
	}
	
	/**
	 * Changes block to its state before change.
	 */
	public void applyRollback()
	{
		BlockState state = this.blockLocation.getBlock().getState();
		state.setType(this.oldMaterial);
		state.setData(this.oldMaterialData);
		state.update(true, false); //Do not apply physics on rollbacks.
	}
	
	/**
	 * Returns the location of block.
	 * 
	 * @return the location
	 */
	public Location getLocation()
	{
		return this.blockLocation;
	}
}
