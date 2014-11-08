package eu.matejkormuth.pexel.master;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that specifies received / sent payload data type.
 */
public enum DataType {
    REQUEST(0),
    RESPONSE(1);
    
    private int type;
    
    private DataType(final int type) {
        this.type = type;
    }
    
    public byte getType() {
        return (byte) this.type;
    }
    
    public static DataType fromType(final byte type) {
        return DataType.type_mapping.get(type);
    }
    
    private static Map<Byte, DataType> type_mapping = new HashMap<Byte, DataType>();
    
    static {
        for (DataType type : DataType.values()) {
            DataType.type_mapping.put(type.getType(), type);
        }
    }
}
