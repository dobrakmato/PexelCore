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
public class V3MetaEntityDamage implements V3Meta {
    private static final int TYPEID = 2;
    
    private final long       internalId;
    private final float      damage;
    
    /**
     * @param internalId
     * @param damage
     */
    public V3MetaEntityDamage(final long internalId, final float damage) {
        this.internalId = internalId;
        this.damage = damage;
    }
    
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        stream.writeLong(this.internalId);
        stream.writeFloat(this.damage);
    }
    
    public static V3MetaEntityDamage readMeta(final DataInputStream stream)
            throws IOException {
        long internalId = stream.readLong();
        float damage = stream.readFloat();
        
        return new V3MetaEntityDamage(internalId, damage);
    }
    
    @Override
    public int getType() {
        return V3MetaEntityDamage.TYPEID;
    }
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaEntityDamage;
    }
    
    /**
     * @return the internalId
     */
    public long getInternalId() {
        return this.internalId;
    }
    
    /**
     * @return the damage
     */
    public float getDamage() {
        return this.damage;
    }
}