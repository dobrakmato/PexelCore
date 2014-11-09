package eu.matejkormuth.pexel.master.requests;

import java.nio.ByteBuffer;

import eu.matejkormuth.pexel.master.Request;

public class SlaveServerMaintenanceModeRequest extends Request {
    byte modeEnabled;
    
    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.allocate(1).put(this.modeEnabled);
    }
    
    @Override
    public void fromByteBuffer(final ByteBuffer buffer) {
        this.modeEnabled = buffer.get();
    }
}
