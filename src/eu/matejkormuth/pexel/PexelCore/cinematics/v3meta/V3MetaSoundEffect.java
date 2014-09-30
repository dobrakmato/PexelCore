package eu.matejkormuth.pexel.PexelCore.cinematics.v3meta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import eu.matejkormuth.pexel.PexelCore.cinematics.V3Meta;
import eu.matejkormuth.pexel.PexelCore.cinematics.V3MetaType;

/**
 * @author M
 * 
 */
public class V3MetaSoundEffect implements V3Meta {
    private static final int TYPEID = 0;
    
    private final double     posX;
    private final double     posY;
    private final double     posZ;
    private final float      pitch;
    private final float      volume;
    private final String     name;
    
    /**
     * Vytvori novy V3MetaSoundEffect so specifikovanymi udajmi.
     * 
     * @param posX
     * @param posY
     * @param posZ
     * @param pitch
     * @param volume
     * @param name
     */
    public V3MetaSoundEffect(final double posX, final double posY, final double posZ,
            final float pitch, final float volume, final String name) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = pitch;
        this.volume = volume;
        this.name = name;
    }
    
    @Override
    public void writeMeta(final DataOutputStream stream) throws IOException {
        stream.writeDouble(this.posX);
        stream.writeDouble(this.posY);
        stream.writeDouble(this.posZ);
        stream.writeFloat(this.volume);
        stream.writeFloat(this.pitch);
        stream.writeUTF(this.name);
    }
    
    public static V3MetaSoundEffect readMeta(final DataInputStream stream)
            throws IOException {
        double x = stream.readDouble();
        double y = stream.readDouble();
        double z = stream.readDouble();
        float volume = stream.readFloat();
        float pitch = stream.readFloat();
        String name = stream.readUTF();
        
        return new V3MetaSoundEffect(x, y, z, pitch, volume, name);
    }
    
    @Override
    public int getType() {
        return V3MetaSoundEffect.TYPEID;
    }
    
    @Override
    public V3MetaType getMetaType() {
        return V3MetaType.V3MetaSoundEffect;
    }
    
    /**
     * @return the posX
     */
    public double getPosX() {
        return this.posX;
    }
    
    /**
     * @return the posY
     */
    public double getPosY() {
        return this.posY;
    }
    
    /**
     * @return the posZ
     */
    public double getPosZ() {
        return this.posZ;
    }
    
    /**
     * @return the pitch
     */
    public float getPitch() {
        return this.pitch;
    }
    
    /**
     * @return the volume
     */
    public float getVolume() {
        return this.volume;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
}
