package eu.matejkormuth.pexel.PexelCore.cinematics;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Interface specifikujuci, ze dana trieda je V3Meta.
 * 
 * @author Mato Kormuth
 * 
 */
public interface V3Meta {
    /**
     * Typ meta.
     */
    public V3MetaType getMetaType();
    
    /**
     * Zapise meta do streamu.
     * 
     * @param stream
     */
    public void writeMeta(DataOutputStream stream) throws IOException;
    
    /**
     * Vrati ciselny typ META.
     * 
     * @return
     */
    public int getType();
}
