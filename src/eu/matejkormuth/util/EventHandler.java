package eu.matejkormuth.util;

/**
 * Generic event handler.
 * 
 * @param <T>
 *            event args type.
 */
public interface EventHandler<T> {
    /**
     * Called when event happens. Handles the event.
     * 
     * @param instance
     *            event args.
     */
    public void handle(T instance);
}