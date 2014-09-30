package eu.matejkormuth.pexel.PexelCore.cinematics;

import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class V3ClipSmoother {
    public static void smoothCompiledClip(final String path, final int a)
            throws Exception {
        V3CameraClip clip = V3CompiledReader.loadFile(path);
        
        clip = V3ClipSmoother.smooth(clip, a);
        
        V3CompiledWriter writer = V3CompiledWriter.createFile(path);
        writer.writeClip(clip);
        writer.close();
    }
    
    public static V3CameraClip smoothClip(final V3CameraClip clip, final int a) {
        return V3ClipSmoother.smooth(clip, a);
    }
    
    /**
     * @param clip
     * @param i
     * @return
     */
    private static V3CameraClip smooth(final V3CameraClip clip, final int a) {
        V3CameraClip newClip = new V3CameraClip();
        
        for (int i = 0; i < clip.frames.size(); i++) {
            V3CameraFrame currentFrame = clip.frames.get(i);
            V3CameraFrame nextFrame = clip.frames.get(i + 1);
            
            newClip.addFrame(currentFrame);
            
            // Smooth movement (X,Y,Z)
            double x = 0;
            double y = 0;
            double z = 0;
            
            // Smooth rotation (Pitch, Yaw)
            float newYaw = (currentFrame.getCameraLocation().getYaw() + nextFrame.getCameraLocation().getYaw()) / 2;
            float newPitch = (currentFrame.getCameraLocation().getPitch() + nextFrame.getCameraLocation().getPitch()) / 2;
            
            Location newLoc = new Location(null, x, y, z, newYaw, newPitch);
            V3CameraFrame newFrame = new V3CameraFrame(newLoc, false);
            
            // Add new frame.
            newClip.addFrame(newFrame);
        }
        
        return newClip;
    }
}
