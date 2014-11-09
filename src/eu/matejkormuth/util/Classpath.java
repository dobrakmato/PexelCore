package eu.matejkormuth.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Classpath {
    public static void addJars(final File folder) throws MalformedURLException,
            ClassNotFoundException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?>[] parameters = new Class[] { URL.class };
        Class<?> sysclass = URLClassLoader.class;
        
        for (File f : folder.listFiles()) {
            try {
                Method method = sysclass.getDeclaredMethod("addURL", parameters);
                method.setAccessible(true);
                method.invoke(sysloader, new Object[] { f.toURI().toURL() });
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
