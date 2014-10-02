package eu.matejkormuth.pexel.PexelCore.particles;

import org.bukkit.entity.Entity;

import eu.matejkormuth.pexel.PexelCore.util.BukkitTimer;

public class EntityAnimationPlayer implements Runnable {
    private final Animation animation;
    private int             currentFrame = 0;
    private final int       frameCount   = 0;
    private BukkitTimer     timer;
    private final Entity    entity;
    private boolean         repeating    = false;
    
    public EntityAnimationPlayer(final Animation animation, final Entity entity,
            final boolean repeating) {
        this.repeating = repeating;
        this.animation = animation;
        this.entity = entity;
    }
    
    public void play() {
        this.timer = new BukkitTimer(1, this);
        this.timer.start();
    }
    
    private void animate() {
        if (this.entity.isDead()) {
            this.timer.stop();
        }
        
        if (this.currentFrame < this.frameCount) {
            this.animation.getFrame(this.currentFrame).play(this.entity.getLocation());
            this.currentFrame++;
        }
        else {
            if (!this.repeating) {
                this.timer.stop();
            }
            else {
                this.currentFrame = 0;
            }
        }
    }
    
    @Override
    public void run() {
        this.animate();
    }
}
