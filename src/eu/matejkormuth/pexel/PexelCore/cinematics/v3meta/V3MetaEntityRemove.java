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
public class V3MetaEntityRemove implements V3Meta {
    private final long internalId;
    
    /**
     * @param internalId2
     */
    public V3MetaEntityRemove(final long internalId2) {
        this.internalId = internalId2;
    }
    
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        stream.writeLong(this.internalId);
    }
    
    public static V3MetaEntityRemove readMeta(final DataInputStream stream)
            throws IOException {
        long internalId = stream.readLong();
        
        return new V3MetaEntityRemove(internalId);
    }
    
    @Override
    public int getType() {
        return 5;
    }
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaEntityRemove;
    }
    
    /**
     * @return the internalId
     */
    public long getInternalId() {
        return this.internalId;
    }
    
}
