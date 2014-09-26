package eu.matejkormuth.pexel.PexelCore.particles;

/**
 * Class that specifies animation.
 */
public interface Animation {
    public Frame getFrame(int number);
    
    public int getFramerate();
    
    public int getFrameCount();
}
