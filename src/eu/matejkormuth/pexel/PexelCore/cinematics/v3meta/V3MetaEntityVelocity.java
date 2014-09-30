package eu.matejkormuth.pexel.PexelCore.cinematics.v3meta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import eu.matejkormuth.pexel.PexelCore.cinematics.V3Meta;
import eu.matejkormuth.pexel.PexelCore.cinematics.V3MetaType;

/**
 * @author Mato Kormuth
 * 
 */
public class V3MetaEntityVelocity implements V3Meta {
    private final long   internalId;
    private final double velX;
    private final double velY;
    private final double velZ;
    
    /**
     * @param internalId
     * @param velX
     * @param velY
     * @param velZ
     */
    public V3MetaEntityVelocity(final long internalId, final double velX,
            final double velY, final double velZ) {
        super();
        this.internalId = internalId;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }
    
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        stream.writeLong(this.internalId);
        stream.writeDouble(this.velX);
        stream.writeDouble(this.velY);
        stream.writeDouble(this.velZ);
    }
    
    public static V3MetaEntityVelocity readMeta(final DataInputStream stream)
            throws IOException {
        long internalId = stream.readLong();
        double velX = stream.readDouble();
        double velY = stream.readDouble();
        double velZ = stream.readDouble();
        
        return new V3MetaEntityVelocity(internalId, velX, velY, velZ);
    }
    
    @Override
    public int getType() {
        return 6;
    }
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaEntityVelocity;
    }
    
    /**
     * @return the internalId
     */
    public long getInternalId() {
        return this.internalId;
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
    
}
