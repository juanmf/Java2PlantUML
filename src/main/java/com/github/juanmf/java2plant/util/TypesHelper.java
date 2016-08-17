package com.github.juanmf.java2plant.util;

import java.net.URLClassLoader;

/**
 * @author juanmf@gmail.com
 */
public class TypesHelper {

    public static String getSimpleName(String fqcn) {
        int lastDotidx = fqcn.lastIndexOf(".");
        String simpleName = -1 == lastDotidx ? fqcn : fqcn.substring(lastDotidx + 1);
        return simpleName;
    }

    public static Class<?> loadClass(String type, URLClassLoader classLoader) {
        try {
            return Class.forName(type, true, classLoader);
        } catch (ClassNotFoundException|NoClassDefFoundError|ExceptionInInitializerError|UnsatisfiedLinkError e) {
            System.out.println(String.format(
                    "Issues loading type %s. \n  Throwed: %s: %s. \n  With Loader: %s", type, e.getClass().getName(),
                    e.getMessage(), null == classLoader ? "null" : classLoader.getClass().getName()
                ));
        } catch (Exception e) {
            System.out.println(String.format(
                    "Unhandled Exception while loading %s!! %s: %s", type, e.getClass().getName(), e.getMessage())
                );
            throw e;
        }
        return null;
    }
}
