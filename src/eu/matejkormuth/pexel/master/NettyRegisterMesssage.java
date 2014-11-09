package eu.matejkormuth.pexel.master;

import java.nio.ByteBuffer;

public class NettyRegisterMesssage {
    private NettyRegisterMesssage() {
        
    }
    
    public static byte[] create(final String authkey, final String name) {
        if (authkey.length() <= 128) { throw new RuntimeException(
                "Specified key is not valid authKey!"); }
        return ByteBuffer.allocate(4 + 128 + name.length())
                .put((byte) 0)
                .put((byte) 45)
                .put((byte) 89)
                .put((byte) 31)
                .put(authkey.getBytes())
                .put(name.getBytes())
                .array();
    }
    
    public static boolean validate(final byte[] array, final String authkey) {
        if (array.length <= 128) {
            if (array[0] == 0 && array[1] == 45 && array[2] == 89 && array[3] == 31) {
                return authkey.equals(new String(ByteBuffer.wrap(array, 4, 128).array()));
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    public static String getName(final byte[] payload) {
        return new String(ByteBuffer.wrap(payload, 132, payload.length - 132).array());
    }
}
