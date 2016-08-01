package com.github.juanmf.java2plant.render.filters;

import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.structure.Use;

/**
 * @author juanmf@gmail.com
 */
public class Filters {
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_USES;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_AGGREGATION;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_EXTENSION;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_ALLOW_ALL_RELATIONS;
    public static final ForbiddenFilter<String> FILTER_ALLOW_ALL_CLASSES;

    static {
        FILTER_FORBID_USES = new ForbiddenFilter<Class<? extends Relation>>();
        FILTER_FORBID_AGGREGATION = new ForbiddenFilter<Class<? extends Relation>>();
        FILTER_FORBID_EXTENSION =  new ForbiddenFilter<Class<? extends Relation>>();
        FILTER_ALLOW_ALL_RELATIONS = new ForbiddenFilter<Class<? extends Relation>>();
        FILTER_ALLOW_ALL_CLASSES = new ForbiddenFilter<String>();

        FILTER_FORBID_USES.addForbidenItem(Use.class);
        FILTER_FORBID_AGGREGATION.addForbidenItem(Aggregation.class);
        FILTER_FORBID_EXTENSION.addForbidenItem(Extension.class);
    }
    
}
