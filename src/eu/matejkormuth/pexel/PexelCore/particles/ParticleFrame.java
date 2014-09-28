package eu.matejkormuth.pexel.PexelCore.particles;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Location;

import eu.matejkormuth.pexel.PexelCore.util.ParticleEffect2;

public class ParticleFrame implements Frame, Collection<ParticleFrame.Particle> {
    private final Collection<ParticleFrame.Particle> particles;
    
    public ParticleFrame(final Collection<ParticleFrame.Particle> particles) {
        this.particles = particles;
    }
    
    public class Particle {
        public double          relX;
        public double          relY;
        public double          relZ;
        public ParticleEffect2 type;
        
        public Particle(final double relX, final double relY, final double relZ,
                final ParticleEffect2 type) {
            super();
            this.relX = relX;
            this.relY = relY;
            this.relZ = relZ;
            this.type = type;
        }
        
        public void play(final Location loc) {
            this.type.display(loc.add(this.relX, this.relY, this.relZ), 0, 0, 0, 1, 1);
        }
    }
    
    public void play(final Location loc) {
        for (Particle p : this.particles) {
            p.play(loc);
        }
    }
    
    @Override
    public int size() {
        return this.particles.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.particles.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.particles.contains(o);
    }
    
    @Override
    public Iterator<Particle> iterator() {
        return this.particles.iterator();
    }
    
    @Override
    public Object[] toArray() {
        return this.particles.toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return this.particles.toArray(a);
    }
    
    @Override
    public boolean add(final Particle e) {
        return this.particles.add(e);
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.particles.remove(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.particles.containsAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Particle> c) {
        return this.particles.addAll(c);
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.particles.removeAll(c);
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.particles.retainAll(c);
    }
    
    @Override
    public void clear() {
        this.particles.clear();
    }
    
}
