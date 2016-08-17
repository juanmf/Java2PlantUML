package com.github.juanmf.java2plant.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Attempts to recover Class name for Arrays so that client code doesn't deal
 * with internal encoding.
 * <p>
 * <code>
 * Examples:
 * String.class.getName()
 * returns "java.lang.String"
 * byte.class.getName()
 * returns "byte"
 * (new Object[3]).getClass().getName()
 * returns "[Ljava.lang.Object;"       <-- this sort of cases cases are handled
 * (new int[3][4][5][6][7][8][9]).getClass().getName()
 * returns "[[[[[[[I"                  <-- this sort of cases cases are handled
 * </code>
 *
 * @author juanmf@gmail.com
 */
public enum CanonicalName {
    Z("boolean", "[Z"),
    B("byte", "[B"),
    C("char", "[C"),
    L("class", "[L"),
    D("double", "[D"),
    F("float", "[F"),
    I("int", "[I"),
    J("long", "[J"),
    S("short", "[S");

    private String className;
    private String code;

    CanonicalName(String className, String code) {
        this.className = className;
        this.code = code;
    }

    public static CanonicalName forCode(String code) {
        for (CanonicalName c: CanonicalName.values()) {
            if (StringUtils.startsWith(code, c.code)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Not an enum constant name: " + code);
    }

    public static String getClassName(String code) {
        CanonicalName cn;
        try {
             cn = forCode(code);
        } catch (IllegalArgumentException e) {
            return code;
        }
        if (cn.equals(CanonicalName.L)) {
            return code.replace(cn.code, "");
        }
        return cn.className;
    }
}
