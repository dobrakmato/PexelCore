/**
 * 
 */
package eu.matejkormuth.pexel.PexelCore.cinematics;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Nahravac tretej verzie.
 * 
 * @author Mato Kormuth
 * 
 */
public class V3BasicRecorder extends BasicRecorder {
    private Player       player;
    private boolean      recording;
    private V3CameraClip clip;
    private long         frames = 0;
    private int          ID     = 0;
    
    /**
     * Pocet FPS v tomto nahravaci.
     */
    public int           FPS    = 20;
    
    public V3BasicRecorder(final Player p, final int fps) {
        super(p, fps);
    }
    
    /**
     * Nastavi ID nahravaca.
     * 
     * @param id
     *            id
     */
    @Override
    public void setID(final int id) {
        this.ID = id;
    }
    
    /**
     * Zacne nahravat.
     */
    @Override
    public void record() {
        this.player.sendMessage("Recording " + ChatColor.RED + "[ID " + this.ID + "] "
                + ChatColor.YELLOW + " (" + this.FPS + "fps) " + ChatColor.GREEN
                + " has started...");
        
        if (this.FPS > 20)
            this.player.sendMessage(ChatColor.RED
                    + "Nahravanie na VIAC AKO 20 FPS nema vyznam! Vysledny zaznam bude rovnaky pri pouziti 20 aj 60 fps.");
        
        this.recording = true;
        
        //Start recording thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                V3BasicRecorder.this._record();
            }
        }).start();
    }
    
    /**
     * Zastavi nahravanie.
     */
    @Override
    public void stop() {
        this.player.sendMessage("Stopped! Recorded " + this.frames + " frames ("
                + (this.frames / this.FPS) + " seconds)...");
        this.recording = false;
        this.clip.save("advrecording" + System.currentTimeMillis());
        this.player.sendMessage("Saved as: advrecording" + System.currentTimeMillis()
                + ".dat");
        this.clip = null;
    }
    
    /**
     * Interna metoda urcena na nahravanie.
     */
    private void _record() {
        while (this.recording) {
            if ((this.frames % 100) == 0) {
                if (this.FPS > 20)
                    this.player.sendMessage(ChatColor.RED
                            + "Nahravanie na VIAC AKO 20 FPS nema vyznam! Vysledny zaznam bude rovnaky pri pouziti 20 aj 60 fps.");
                this.player.sendMessage("Recorded " + this.frames + " frames ("
                        + (this.frames / this.FPS) + " seconds)");
            }
            
            //Pridaj frame.
            this.clip.addFrame(new V3CameraFrame(this.player.getLocation(), false));
            this.frames++;
            
            if (this.frames > 30000) {
                //prekrocene maximum
                this.player.sendMessage("Zastavujem nahravanie. Prekrocene maximum frameov.");
                this.stop();
            }
            
            try {
                Thread.sleep(1000 / this.FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Vrati player.
     * 
     * @return player
     */
    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Nastavi hraca.
     * 
     * @param player
     *            hrac
     */
    @Override
    public void setPlayer(final Player player) {
        this.player = player;
    }
}
