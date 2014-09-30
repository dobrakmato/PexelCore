package eu.matejkormuth.pexel.PexelCore.cinematics;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import eu.matejkormuth.pexel.PexelCore.Pexel;

/**
 * @author Mato Kormuth
 * 
 */
public class V3Compiler implements Listener {
    private final V3CompiledWriter writer;
    
    // private String outPath;
    
    /**
     * Skompiluje V3 klip do kompilovanej formy za pomoci hraca.
     * 
     * @param outPath
     * @param clip
     */
    public V3Compiler(final String outPath) {
        // Priprav writer.
        this.writer = V3CompiledWriter.createFile(outPath);
        // this.outPath = outPath;
        // Priprav handlovanie eventov.
        Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
    }
    
    /**
     * Skompiluje neskompilovany klip.
     * 
     * @param clip
     *            nezostaveny klip.
     * @param camera
     *            hrac, ktory bude pouzity na zostavovanie.
     */
    public void scheduleCompilation(final V3CameraClip clip, final Player camera) {
        // Vypisat varovanie.
        this.broadcast("Bola naplanovana kompilacia na hraca " + camera.getName());
        // this.broadcast("Vystup: " + this.outPath);
        this.broadcast(ChatColor.YELLOW + "Kompilacia zacina za 15 sekund...");
        this.notifyBySound();
        
        V3Compiler.this.broadcast(ChatColor.GREEN + "Kompilacia zacina za 0 sekund...");
        V3Compiler.this.compile();
    }
    
    /**
	 * 
	 */
    private void notifyBySound() {
        // Upozorni vsetkych hracov na kompilaciu.
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.NOTE_BASS, Float.MAX_VALUE, 0);
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, Float.MAX_VALUE, 0);
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, Float.MAX_VALUE, 1);
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, Float.MAX_VALUE, 2);
        }
    }
    
    private void soundError() {
        
    }
    
    /**
     * Zacne zostavovanie cinematicu.
     */
    protected void compile() {
        // Zaciatok.
        this.error("Tato operacia (compile) nie je podporovana!");
        
        // Na konci unloadni dolezite veci, uloz subor a zrus eventy.
        this.finishCompiling();
    }
    
    /**
     * Volane na konci kompilacie.
     */
    private void finishCompiling() {
        this.broadcast(ChatColor.GREEN + "Kompilacia dokoncena! Upratujem bordel...");
        // Odregistruj eventy.
        this.unregisterEvents();
        // Zatvor subor.
        try {
            this.writer.close();
        } catch (IOException e) {
            this.error(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Vypise do chatu spravu.
     * 
     * @param message
     */
    private void broadcast(final String message) {
        Bukkit.broadcastMessage(ChatColor.BLUE + "[V3cam] " + ChatColor.WHITE + message);
    }
    
    /**
     * Vypise do chatu chybu.
     * 
     * @param message
     */
    private void error(final String message) {
        this.soundError();
        Bukkit.broadcastMessage(ChatColor.BLUE + "[V3cam] " + ChatColor.RED + message);
    }
    
    /**
     * Odregistruje udalosti.
     */
    private void unregisterEvents() {
        // Treba unregistrovat eventy, inak sa server zblazni.
    }
}
