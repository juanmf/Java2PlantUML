package com.github.juanmf.java2plant.structure;

/**
 * @author juanmf@gmail.com
 */
public class Implementation extends Extension {
    public static final String RELATION_TYPE_EXTENSION = " ..up|> ";

    public Implementation(Class<?> from, String to) {
        super(from, to);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getFromType().getName(), RELATION_TYPE_EXTENSION, getToType());
    }
}
