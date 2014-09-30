package eu.matejkormuth.pexel.PexelCore.cinematics.v3meta;

import java.io.DataOutputStream;
import java.io.IOException;

import eu.matejkormuth.pexel.PexelCore.cinematics.V3Meta;
import eu.matejkormuth.pexel.PexelCore.cinematics.V3MetaType;

/**
 * @author Mato Kormuth
 * 
 */
public class V3MetaExplosion implements V3Meta {
    private double posX;
    private double posY;
    private double posZ;
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaExplosion;
    }
    
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        
    }
    
    @Override
    public int getType() {
        return 0;
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
    
}
