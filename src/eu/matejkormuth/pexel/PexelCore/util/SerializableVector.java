package eu.matejkormuth.pexel.PexelCore.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.bukkit.util.Vector;

/**
 * Vector that is serializable.
 * 
 * @author Mato Kormuth
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializableVector extends Vector {
    @XmlAttribute(name = "x")
    protected double x;
    @XmlAttribute(name = "y")
    protected double y;
    @XmlAttribute(name = "z")
    protected double z;
    
    public SerializableVector(final Vector vector) {
        super(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public SerializableVector(final int x, final int y, final int z) {
        super(x, y, z);
    }
    
    public SerializableVector(final double x, final double y, final double z) {
        super(x, y, z);
    }
    
    public SerializableVector(final float x, final float y, final float z) {
        super(x, y, z);
    }
}
