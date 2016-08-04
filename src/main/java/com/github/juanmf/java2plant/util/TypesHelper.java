package com.github.juanmf.java2plant.util;

/**
 * @author juanmf@gmail.com
 */
public class TypesHelper {

    public static String getSimpleName(String fqcn) {
        int lastDotidx = fqcn.lastIndexOf(".");
        String simpleName = -1 == lastDotidx ? fqcn : fqcn.substring(lastDotidx + 1);
        return simpleName;
    }

}
