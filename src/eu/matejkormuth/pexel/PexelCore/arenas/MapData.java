package eu.matejkormuth.pexel.PexelCore.arenas;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.bukkit.Location;

import eu.matejkormuth.pexel.PexelCore.core.Region;
import eu.matejkormuth.pexel.PexelCore.util.SerializableLocation;

/**
 * Class that represents playable map.
 * 
 * @author Mato Kormuth
 * 
 */
@XmlType(name = "arenamap")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MapData {
    @XmlAttribute(name = "name")
    protected String                                  name;
    @XmlAttribute(name = "minigameName")
    protected String                                  minigameName;
    
    @XmlElementWrapper(name = "options")
    protected final Map<String, String>               options   = new HashMap<String, String>();
    @XmlElementWrapper(name = "locations")
    protected final Map<String, SerializableLocation> locations = new HashMap<String, SerializableLocation>();
    @XmlElementWrapper(name = "regions")
    protected final Map<String, Region>               regions   = new HashMap<String, Region>();
    
    public static final MapData load(final File file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(MapData.class);
        Unmarshaller un = jc.createUnmarshaller();
        return (MapData) un.unmarshal(file);
    }
    
    public void save(final File file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(MapData.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(this, file);
    }
    
    public boolean validate(final MinigameArena arena) {
        return arena.getMinigame().getName().equals(this.minigameName);
    }
    
    public String getOption(final String key) {
        return this.options.get(key);
    }
    
    public String getMinigameName() {
        return this.minigameName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Location getLocation(final String key) {
        return this.locations.get(key).getLocation();
    }
    
    public Region getRegion(final String key) {
        return this.regions.get(key);
    }
    
    public Map<String, String> getOptions() {
        return this.options;
    }
    
    public Map<String, SerializableLocation> getLocations() {
        return this.locations;
    }
    
    public Map<String, Region> getRegions() {
        return this.regions;
    }
}
