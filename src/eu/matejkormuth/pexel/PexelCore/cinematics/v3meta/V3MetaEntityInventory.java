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
public class V3MetaEntityInventory implements V3Meta {
    private final long internalId;
    private final byte slot;
    private final int  itemType;
    
    /**
     * @param internalId
     * @param slot
     * @param type
     */
    public V3MetaEntityInventory(final long internalId, final byte slot, final int type) {
        super();
        this.internalId = internalId;
        this.slot = slot;
        this.itemType = type;
    }
    
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        stream.writeLong(this.internalId);
        stream.writeByte(this.slot);
        stream.writeInt(this.itemType);
    }
    
    public static V3MetaEntityInventory readMeta(final DataInputStream stream)
            throws IOException {
        long internalId = stream.readLong();
        byte slot = stream.readByte();
        int type = stream.readInt();
        
        return new V3MetaEntityInventory(internalId, slot, type);
    }
    
    @Override
    public int getType() {
        return 4;
    }
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaEntityInventory;
    }
    
    /**
     * @return the internalId
     */
    public long getInternalId() {
        return this.internalId;
    }
    
    /**
     * @return the slot
     */
    public byte getSlot() {
        return this.slot;
    }
    
    /**
     * @return the itemType
     */
    public int getItemType() {
        return this.itemType;
    }
}
