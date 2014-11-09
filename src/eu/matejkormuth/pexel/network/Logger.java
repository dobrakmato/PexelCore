package eu.matejkormuth.pexel.network;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that represents logger.
 */
public class Logger {
    private SimpleDateFormat format;
    private final Logger     parent;
    private final String     name;
    
    public Logger(final String name) {
        this.name = name;
        this.parent = null;
    }
    
    private Logger(final Logger parent, final String name) {
        this.parent = parent;
        this.name = name;
    }
    
    public void info(final String msg) {
        if (this.parent == null) {
            this.log("[INFO] " + msg);
        }
        else {
            this.parent.info("[" + this.name + "] " + msg);
        }
    }
    
    public void error(final String msg) {
        if (this.parent == null) {
            this.log("[ERROR] " + msg);
        }
        else {
            this.parent.error("[" + this.name + "] " + msg);
        }
    }
    
    public void warn(final String msg) {
        if (this.parent == null) {
            this.log("[WARN] " + msg);
        }
        else {
            this.parent.warn("[" + this.name + "] " + msg);
        }
    }
    
    public void debug(final String msg) {
        if (this.parent == null) {
            this.log("[DEBUG] " + msg);
        }
        else {
            this.parent.debug("[" + this.name + "] " + msg);
        }
    }
    
    public Logger getChild(final String name) {
        return new Logger(this, name);
    }
    
    protected void log(final String msg) {
        System.out.println(this.timeStamp() + " [" + this.name + "] " + msg);
    }
    
    private String timeStamp() {
        if (this.format == null) {
            this.format = new SimpleDateFormat("HH:mm:ss");
        }
        return "[" + this.format.format(new Date()) + "]";
    }
}
