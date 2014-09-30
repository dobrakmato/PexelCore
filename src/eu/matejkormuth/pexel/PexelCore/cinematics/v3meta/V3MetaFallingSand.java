package eu.matejkormuth.pexel.PexelCore.cinematics.v3meta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Material;

import eu.matejkormuth.pexel.PexelCore.cinematics.V3Meta;
import eu.matejkormuth.pexel.PexelCore.cinematics.V3MetaType;

/**
 * @author Mato Kormuth
 * 
 */
public class V3MetaFallingSand implements V3Meta {
    private final double   posX;
    private final double   posY;
    private final double   posZ;
    private final double   velX;
    private final double   velY;
    private final double   velZ;
    private final Material material;
    
    /**
     * @param posX
     * @param posY
     * @param posZ
     * @param velX
     * @param velY
     * @param velZ
     * @param material
     */
    public V3MetaFallingSand(final double posX, final double posY, final double posZ,
            final double velX, final double velY, final double velZ,
            final Material material) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.material = material;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        stream.writeDouble(this.posX);
        stream.writeDouble(this.posY);
        stream.writeDouble(this.posZ);
        stream.writeDouble(this.velX);
        stream.writeDouble(this.velY);
        stream.writeDouble(this.velZ);
        stream.writeInt(this.material.getId());
    }
    
    @SuppressWarnings("deprecation")
    public static V3MetaFallingSand readMeta(final DataInputStream stream)
            throws IOException {
        double posX = stream.readDouble();
        double posY = stream.readDouble();
        double posZ = stream.readDouble();
        double velX = stream.readDouble();
        double velY = stream.readDouble();
        double velZ = stream.readDouble();
        Material material = Material.getMaterial(stream.readInt());
        
        return new V3MetaFallingSand(posX, posY, posZ, velX, velY, velZ, material);
    }
    
    @Override
    public int getType() {
        return 8;
    }
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaFallingSand;
    }
    
    /**
     * @return the posX
     */
    public double getPosX() {
        return this.posX;
    }
    
    /**
     * @return the posY
     */
    public double getPosY() {
        return this.posY;
    }
    
    /**
     * @return the posZ
     */
    public double getPosZ() {
        return this.posZ;
    }
    
    /**
     * @return the velX
     */
    public double getVelX() {
        return this.velX;
    }
    
    /**
     * @return the velY
     */
    public double getVelY() {
        return this.velY;
    }
    
    /**
     * @return the velZ
     */
    public double getVelZ() {
        return this.velZ;
    }
    
    /**
     * @return the material
     */
    public Material getMaterial() {
        return this.material;
    }
    
}
