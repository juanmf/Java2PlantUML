package com.github.juanmf.java2plant.util;

import java.net.URLClassLoader;

/**
 * @author juanmf@gmail.com
 */
public class TypesHelper {
    private static final String REGEX_FOR_PACKAGE = "((([ice])(nterface|lass|num))? ?([\\w\\[][_\\w\\d\\$]+\\.)+)";

    /**
     * Should return a decent short version of the FQCN given:
     *
     * <code>
     *     class java.lang.String -> c String
     *     class java.lang.Class<? extends java.net.URLClassLoader> -> c Class<? extends URLClassLoader>
     *     interface java.util.Collection<java.lang.Class<?>> -> i Collection<Class<?>>
     * </code>
     *
     * @param fqcn
     * @return
     */
    public static String getSimpleName(String fqcn) {
        return fqcn.replaceAll(REGEX_FOR_PACKAGE, "$3 ");
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
