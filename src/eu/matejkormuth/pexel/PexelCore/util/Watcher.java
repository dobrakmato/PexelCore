package eu.matejkormuth.pexel.PexelCore.util;

public class Watcher<T> {
    private T        lastState;
    private Runnable onChanged;
    
    public void check(final T value) {
        if (this.changed(value)) {
            this.lastState = value;
            this.onChanged.run();
        }
    }
    
    public boolean changed(final T value) {
        return this.lastState == value;
    }
    
    public void setOnChanged(final Runnable onChanged) {
        this.onChanged = onChanged;
    }
}
