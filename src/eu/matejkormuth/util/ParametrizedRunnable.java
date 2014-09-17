package eu.matejkormuth.util;

/**
 * Parametrized runnable.
 */
public interface ParametrizedRunnable {
    /**
     * Runs action with specified parameters.
     * 
     * @param params
     *            parameters
     */
    public void run(Object... params);
}
