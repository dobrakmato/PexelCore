package eu.matejkormuth.pexel.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class that represents handle.
 */
public class Handle {
    protected Method method;
    protected Object object;
    
    public Handle(final Object object, final Method method) {
        this.object = object;
        this.method = method;
    }
    
    public Object invoke(final Object... args) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return this.method.invoke(this.object, args);
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public Object getObject() {
        return this.object;
    }
}
