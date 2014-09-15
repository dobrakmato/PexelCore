package eu.matejkormuth.pexel.PexelCore.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Location that is serializable.
 * 
 * @author Mato Kormuth
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializableLocation {
    @XmlAttribute(name = "x")
    protected double X;
    @XmlAttribute(name = "y")
    protected double Y;
    @XmlAttribute(name = "z")
    protected double Z;
    @XmlAttribute(name = "yaw")
    protected float  yaw;
    @XmlAttribute(name = "pitch")
    protected float  pitch;
    @XmlAttribute(name = "world")
    protected String worldName;
    @XmlTransient
    private Location location;
    
    public SerializableLocation(final double x, final double y, final double z,
            final float yaw, final float pitch, final String worldName) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldName = worldName;
    }
    
    public SerializableLocation(final Location location) {
        this.location = location;
        this.X = location.getX();
        this.Y = location.getY();
        this.Z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.worldName = location.getWorld().getName();
    }
    
    public static final SerializableLocation fromLocation(final Location location) {
        return new SerializableLocation(location);
    }
    
    public Location getLocation() {
        if (this.location == null)
            this.create();
        return this.location;
    }
    
    private void create() {
        this.location = new Location(Bukkit.getWorld(this.worldName), this.X, this.Y,
                this.Z, this.yaw, this.pitch);
    }
}
