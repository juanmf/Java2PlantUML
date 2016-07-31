package com.github.juanmf.java2plant.structure;

/**
 * @author juanmf@gmail.com
 */
public interface Relation {
    String getFromType();
    String getToType();
    String getRelationType();
    String getMessage();
    String getFromCardinal();
    String getToCardinal();
}
