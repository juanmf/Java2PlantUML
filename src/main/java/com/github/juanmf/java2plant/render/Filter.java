package com.github.juanmf.java2plant.render;

import com.github.juanmf.java2plant.structure.Relation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author juanmf@gmail.com
 */
public class Filter {
    private Map<Class<? extends Relation>, Boolean> forbidenRelations = new HashMap<>();

    public void addForbidenRelation(Class<? extends Relation> relation) {
        forbidenRelations.put(relation, true);
    }

    public boolean isForbidenRelation(Class<? extends Relation> relation) {
        return forbidenRelations.containsKey(relation);
    }
}
