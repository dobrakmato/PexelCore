package me.dobrakmato.plugins.pexel.PexelCore.util;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;

/**
 * Class used for countdown.
 * 
 * @author Mato Kormuth
 * 
 */
public class Countdown {
    private int      timeLeft   = 0;
    private int      timeLenght = 0;
    private int      taskId     = 0;
    private Runnable onEnd;
    private Runnable onTick;
    private String   tag        = null;
    
    /**
     * Creates new countdown with specified time left.
     * 
     * @param seconds
     *            time lieft in seconds
     */
    public Countdown(final int seconds) {
        this.timeLenght = seconds;
        this.timeLeft = seconds;
    }
    
    /**
     * Creates new countdown with specified time left.
     * 
     * @param seconds
     *            time lieft in seconds
     * @param tag
     *            tag of countdown
     */
    public Countdown(final int seconds, final String tag) {
        this.timeLenght = seconds;
        this.timeLeft = seconds;
        this.tag = tag;
    }
    
    /**
     * Starts the countdown.
     */
    public void start() {
        this.taskId = Pexel.getScheduler().scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {
                Countdown.this.tick();
            }
        }, 0L, 20L);
    }
    
    private void tick() {
        this.timeLeft--;
        if (this.onTick != null)
            this.onTick.run();
        
        if (this.timeLeft < 1) {
            Pexel.cancelTask(this.taskId);
            if (this.onEnd != null)
                this.onEnd.run();
        }
    }
    
    /**
     * Pauses countdown. Resume with {@link Countdown#start()}.
     */
    public void pause() {
        Pexel.cancelTask(this.taskId);
    }
    
    /**
     * Resets time left to default value.
     */
    public void reset() {
        this.timeLeft = this.timeLenght;
    }
    
    /**
     * Sets runnable that will be executed when countdown reach zero.
     * 
     * @param onEnd
     *            runnable
     */
    public void setOnEnd(final Runnable onEnd) {
        this.onEnd = onEnd;
    }
    
    /**
     * Sets runnable that will be executed each second.
     * 
     * @param onTick
     *            runnable
     */
    public void setOnTick(final Runnable onTick) {
        this.onTick = onTick;
    }
    
    /**
     * Returns tag of this countdown.
     * 
     * @return tag
     */
    public String getTag() {
        return this.tag;
    }
    
    /**
     * Returns time left in countdown in seconds.
     * 
     * @return time left in seconds
     */
    public int getTimeLeft() {
        return this.timeLeft;
    }
    
    /**
     * Returns lenght of countdown.
     * 
     * @return lenght
     */
    public int getLenght() {
        return this.timeLenght;
    }
}
