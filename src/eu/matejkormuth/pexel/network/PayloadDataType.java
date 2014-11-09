package eu.matejkormuth.pexel.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that specifies received / sent payload data type.
 */
public enum PayloadDataType {
    REQUEST(0),
    RESPONSE(1),
    OTHER(127);
    
    private int type;
    
    private PayloadDataType(final int type) {
        this.type = type;
    }
    
    public byte getType() {
        return (byte) this.type;
    }
    
    public static PayloadDataType fromType(final byte type) {
        return PayloadDataType.type_mapping.get(type);
    }
    
    private static Map<Byte, PayloadDataType> type_mapping = new HashMap<Byte, PayloadDataType>();
    
    static {
        for (PayloadDataType type : PayloadDataType.values()) {
            PayloadDataType.type_mapping.put(type.getType(), type);
        }
    }
}
