package eu.matejkormuth.pexel.master;

import java.nio.ByteBuffer;

public class NoArgRequest extends Request {
    
    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.allocate(1).put((byte) 1);
    }
    
    @Override
    public void fromByteBuffer(final ByteBuffer buffer) {
        
    }
}
