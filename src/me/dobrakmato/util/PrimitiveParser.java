package me.dobrakmato.util;

public final class PrimitiveParser {
    public static final Object parseAsObject(final String string,
            final PrimitiveType asType) {
        switch (asType) {
            case BOOLEAN:
                return PrimitiveParser.parseBoolean(string);
            case BYTE:
                return PrimitiveParser.parseByte(string);
            case DOUBLE:
                return PrimitiveParser.parseDouble(string);
            case FLOAT:
                return PrimitiveParser.parseFloat(string);
            case INTEGER:
                return PrimitiveParser.parseInteger(string);
            case LONG:
                return PrimitiveParser.parseLong(string);
            case SHORT:
                return PrimitiveParser.parseShort(string);
            default:
                return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> T parse(final String string, final PrimitiveType asType) {
        switch (asType) {
            case BOOLEAN:
                return (T) (Boolean) PrimitiveParser.parseBoolean(string);
            case BYTE:
                return (T) (Byte) PrimitiveParser.parseByte(string);
            case DOUBLE:
                return (T) (Double) PrimitiveParser.parseDouble(string);
            case FLOAT:
                return (T) (Float) PrimitiveParser.parseFloat(string);
            case INTEGER:
                return (T) (Integer) PrimitiveParser.parseInteger(string);
            case LONG:
                return (T) (Long) PrimitiveParser.parseLong(string);
            case SHORT:
                return (T) (Short) PrimitiveParser.parseShort(string);
            default:
                return null;
        }
    }
    
    public static final double parseDouble(final String string) {
        return Double.parseDouble(string);
    }
    
    public static final float parseFloat(final String string) {
        return Float.parseFloat(string);
    }
    
    public static final byte parseByte(final String string) {
        return Byte.parseByte(string);
    }
    
    public static final boolean parseBoolean(final String string) {
        return Boolean.parseBoolean(string);
    }
    
    public static final int parseInteger(final String string) {
        return Integer.parseInt(string);
    }
    
    public static final short parseShort(final String string) {
        return Short.parseShort(string);
    }
    
    public static final long parseLong(final String string) {
        return Long.parseLong(string);
    }
    
    public static enum PrimitiveType {
        FLOAT,
        DOUBLE,
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        BOOLEAN;
    }
}
