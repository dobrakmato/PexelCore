package eu.matejkormuth.pexel.PexelCore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import eu.matejkormuth.pexel.PexelCore.core.Log;
import eu.matejkormuth.pexel.PexelCore.core.Paths;

/**
 * Class used for caching data.
 */
public class CacheHelper {
    private Map<String, Object> cache = new HashMap<>();
    private final String        name;
    private boolean             nr    = false;
    
    public CacheHelper(final String name) {
        this.name = name;
        try {
            this.load();
        } catch (ClassNotFoundException | IOException e) {
            this.nr = true;
            Log.severe("Cache broken!");
            e.printStackTrace();
        }
    }
    
    public boolean needsRebuild() {
        return this.nr;
    }
    
    @SuppressWarnings("unchecked")
    private void load() throws FileNotFoundException, IOException,
            ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(
                Paths.cache(this.name))));
        Object object = ois.readObject();
        ois.close();
        if (object instanceof HashMap<?, ?>)
            this.cache = (HashMap<String, Object>) object;
        else
            throw new RuntimeException("Broken cache file! Can't read data!");
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSize() {
        return (int) new File(Paths.cache(this.name)).length();
    }
    
    public void clear() {
        this.cache.clear();
    }
    
    public void put(final String key, final Object value) {
        this.cache.put(key, value);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getTyped(final String key) {
        return (T) this.cache.get(key);
    }
    
    public Object get(final String key) {
        return this.cache.get(key);
    }
    
    public void commit() throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(
                Paths.cache(this.name))));
        oos.writeObject(this.cache);
        oos.close();
    }
    
}
