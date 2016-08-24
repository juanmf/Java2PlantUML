package com.github.juanmf.java2plant.structure;

import com.github.juanmf.java2plant.util.TypesHelper;

/**
 * @author juanmf@gmail.com
 */
public class Implementation extends Extension {
    public static final String RELATION_TYPE_IMPLEMENTATION = " ..up|> ";
    public static final String RELATION_TYPE_LOLLIPOP = " -() ";

    public Implementation(Class<?> from, String to) {
        super(from, to);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getFromType().getName(), RELATION_TYPE_IMPLEMENTATION, getToType());
    }

    public String asLollipop() {
        return String.format("\"%s\" %s %s", getFromType().getName(), RELATION_TYPE_LOLLIPOP,
                TypesHelper.getSimpleName(getToType()));
    }
}
