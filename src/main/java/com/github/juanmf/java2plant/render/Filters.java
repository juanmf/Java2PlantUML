package com.github.juanmf.java2plant.render;

import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Use;

/**
 * @author juanmf@gmail.com
 */
public class Filters {
    public static final Filter FILTER_FORBID_USES;
    public static final Filter FILTER_FORBID_AGGREGATION;
    public static final Filter FILTER_FORBID_EXTENSION;
    public static final Filter FILTER_ALLOW_ALL;

    static {
        FILTER_FORBID_USES = new Filter();
        FILTER_FORBID_AGGREGATION = new Filter();
        FILTER_FORBID_EXTENSION =  new Filter();
        FILTER_ALLOW_ALL = new Filter();

        FILTER_FORBID_USES.addForbidenRelation(Use.class);
        FILTER_FORBID_AGGREGATION.addForbidenRelation(Aggregation.class);
        FILTER_FORBID_EXTENSION.addForbidenRelation(Extension.class);
    }
}
