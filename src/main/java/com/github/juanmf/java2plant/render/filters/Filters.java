package com.github.juanmf.java2plant.render.filters;

import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.structure.Use;

import java.util.regex.Pattern;

/**
 * @author juanmf@gmail.com
 */
public class Filters {
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_USES;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_AGGREGATION;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_EXTENSION;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_ALLOW_ALL_RELATIONS;
    public static final ForbiddenFilter<Class<?>> FILTER_ALLOW_ALL_CLASSES;

    public static final ForbiddenRexegFilter<Class<?>> FILTER_FORBID_ANONIMOUS;
    public static final ForbiddenRexegFilter<Class<?>> FILTER_FORBID_PRIMITIVES;
    public static final ForbiddenRexegFilter<Class<?>> FILTER_FORBID_BASE_CLASSES;

    public static final RelationFieldsFilter FILTER_RELATION_FORBID_TO_PRIMITIVE;
    public static final RelationFieldsFilter FILTER_RELATION_FORBID_TO_BASE;
    public static final RelationFieldsFilter FILTER_RELATION_FORBID_FROM_ANONIMOUS;
    public static final RelationFieldsFilter FILTER_RELATION_ALLOW_ALL;

    public static final ChainFilter<Relation> FILTER_CHAIN_RELATION_STANDARD;

    static {
        FILTER_FORBID_USES = new ForbiddenFilter<>();
        FILTER_FORBID_AGGREGATION = new ForbiddenFilter<>();
        FILTER_FORBID_EXTENSION =  new ForbiddenFilter<>();
        FILTER_ALLOW_ALL_RELATIONS = new ForbiddenFilter<>();
        FILTER_ALLOW_ALL_CLASSES = new ForbiddenFilter<>();
        FILTER_FORBID_ANONIMOUS = new ForbiddenRexegFilter();
        FILTER_CHAIN_RELATION_STANDARD = new ChainFilter<>();
        FILTER_FORBID_PRIMITIVES = new ForbiddenRexegFilter<>();
        FILTER_FORBID_BASE_CLASSES = new ForbiddenRexegFilter<>();
        FILTER_RELATION_FORBID_TO_PRIMITIVE = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.TO);
        FILTER_RELATION_FORBID_TO_BASE = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.TO);
        FILTER_RELATION_FORBID_FROM_ANONIMOUS = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.FROM);
        FILTER_RELATION_ALLOW_ALL = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.FROM);

        FILTER_FORBID_USES.addForbiddenItem(Use.class);
        FILTER_FORBID_AGGREGATION.addForbiddenItem(Aggregation.class);
        FILTER_FORBID_EXTENSION.addForbiddenItem(Extension.class);
        FILTER_FORBID_ANONIMOUS.addForbiddenItem(Pattern.compile(".*\\$\\d.*"));
        FILTER_FORBID_PRIMITIVES.addForbiddenItem(Pattern.compile("[^\\.]"));
        FILTER_FORBID_BASE_CLASSES.addForbiddenItem(Pattern.compile("^java\\.lang\\..*"));

        FILTER_RELATION_FORBID_TO_PRIMITIVE.setFilter(FILTER_FORBID_PRIMITIVES);
        FILTER_RELATION_FORBID_TO_BASE.setFilter(FILTER_FORBID_BASE_CLASSES);
        FILTER_RELATION_FORBID_FROM_ANONIMOUS.setFilter(FILTER_FORBID_ANONIMOUS);
        FILTER_RELATION_ALLOW_ALL.setFilter(FILTER_ALLOW_ALL_CLASSES);

        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_TO_PRIMITIVE);
        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_TO_BASE);
        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_FROM_ANONIMOUS);
    }
}
