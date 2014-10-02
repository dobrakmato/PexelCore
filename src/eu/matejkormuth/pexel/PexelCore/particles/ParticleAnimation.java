package eu.matejkormuth.pexel.PexelCore.particles;

import java.io.Serializable;
import java.util.ArrayList;

public class ParticleAnimation implements Animation, Serializable {
    private static final long              serialVersionUID = 5147694021142531376L;
    
    private final ArrayList<ParticleFrame> frames           = new ArrayList<ParticleFrame>();
    private final int                      framerate        = 20;
    
    @Override
    public ParticleFrame getFrame(final int index) {
        return this.frames.get(index);
    }
    
    @Override
    public int getFramerate() {
        return this.framerate;
    }
    
    @Override
    public int getFrameCount() {
        return this.frames.size();
    }
}
