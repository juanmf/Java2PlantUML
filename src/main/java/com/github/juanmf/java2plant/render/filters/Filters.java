package com.github.juanmf.java2plant.render.filters;

import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.render.PlantRenderer;
import com.github.juanmf.java2plant.render.event.RejectingBaseInterfaceEvent;
import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.structure.Use;
import com.github.juanmf.java2plant.util.TypesHelper;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Filters control the output by providing a configurable way to limit noise.
 * These filters are applied in key rendering points by {@link PlantRenderer#render()}
 *
 * There are of three kinds:
 * <pre>
 *  Classes: prevent printing Class<?> objects that match some criteria.
 *  Relation:  prevent printing Relations whose from or To side objects matches some criteria.
 *  RelationTypes: prevent printing relations by their type, this is a coarse approach.
 * </pre>
 *
 * Chain filters.
 * To customize output further more, youc an use Chain filters of each of the three
 * filter types, achievinw a complex behavior by combination of simpler filters.
 * e.g. {@link #FILTER_CHAIN_RELATION_STANDARD}
 *
 * PredicateFilters.
 * You can define filters of any of the three types, that will apply a custom logic to
 * the given object. e.g. {@link #FILTER_RELATION_FORBID_AGGREGATION_FROM_PRIVATE}
 *
 * @author juanmf@gmail.com
 */
public class Filters {
    public static final String CHAIN_CLASSES_CUSTOM_NAME = "FILTER_CHAIN_CLASSES_CUSTOM";
    public static final String CHAIN_RELATIONS_CUSTOM_NAME = "FILTER_CHAIN_RELATION_CUSTOM";
    public static final String CHAIN_RELATION_TYPE_CUSTOM_NAME = "FILTER_CHAIN_RELATION_TYPE_CUSTOM";

    /**
     * Can be assigned to {@link com.github.juanmf.java2plant.render.PlantRenderer#relationsTypeFilter}
     * that corresponds to {@link com.github.juanmf.java2plant.goal.Parse#relationTypeFilter}
     */
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_USES;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_AGGREGATION;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_FORBID_EXTENSION;
    public static final ForbiddenFilter<Class<? extends Relation>> FILTER_ALLOW_ALL_RELATIONS;

    /**
     * Aggregation of several Filter<Class<? extends Relation>> Filters to enable compound behavior to
     * {@link com.github.juanmf.java2plant.render.PlantRenderer#relationsTypeFilter}
     */
    public static final ChainFilter<Class<? extends Relation>> FILTER_CHAIN_RELATION_TYPE_STANDARD;
    private static final ChainFilter<Class<? extends Relation>> FILTER_CHAIN_RELATION_TYPE_CUSTOM;

    /**
     * Can be assigned to {@link com.github.juanmf.java2plant.render.PlantRenderer#relationsFilter}
     * that corresponds to {@link com.github.juanmf.java2plant.goal.Parse#relationsFilter}
     */
    public static final PredicateFilter<Relation> FILTER_FORBID_ENUM_AGGREGATION_LOOP;
    public static final PredicateFilter<Relation> FILTER_RELATION_FORBID_AGGREGATION_FROM_PRIVATE;
    public static final RelationFieldsFilter FILTER_RELATION_FORBID_TO_PRIMITIVE;
    public static final RelationFieldsFilter FILTER_RELATION_FORBID_TO_BASE;
    public static final RelationFieldsFilter FILTER_RELATION_FORBID_FROM_BASE;
    public static final RelationFieldsFilter FILTER_RELATION_FORBID_FROM_ANONIMOUS;
    public static final RelationFieldsFilter FILTER_RELATION_ALLOW_ALL;

    /**
     * Aggregation of several Filter<Relation> Filters to enable compound behavior to
     * {@link com.github.juanmf.java2plant.render.PlantRenderer#relationsFilter}
     */
    public static final ChainFilter<Relation> FILTER_CHAIN_RELATION_STANDARD;
    private static final ChainFilter<Relation> FILTER_CHAIN_RELATION_CUSTOM;

    /**
     * Can be assigned to {@link com.github.juanmf.java2plant.render.PlantRenderer#classesFilter}
     * that corresponds to {@link com.github.juanmf.java2plant.goal.Parse#classesFilter}
     */
    public static final ForbiddenFilter<Class<?>> FILTER_ALLOW_ALL_CLASSES;
    public static final ForbiddenRexegFilter<Class<?>> FILTER_FORBID_ANONIMOUS;
    public static final ForbiddenRexegFilter<Class<?>> FILTER_FORBID_PRIMITIVES;
    public static final ForbiddenRexegFilter<Class<?>> FILTER_FORBID_BASE_CLASSES;

    /**
     * Aggregation of several Filter<Class<?>> Filters to enable compound behavior to
     * {@link com.github.juanmf.java2plant.render.PlantRenderer#classesFilter}
     */
    public static final ChainFilter<Class<?>> FILTER_CHAIN_CLASSES_STANDARD;
    private static final ChainFilter<Class<?>> FILTER_CHAIN_CLASSES_CUSTOM;


    public static final Map<String, Filter> FILTERS = new HashMap<>();

    /**
     * Instantiate Filters
     */
    static {
        FILTER_FORBID_USES = new ForbiddenFilter<>();
        FILTER_FORBID_AGGREGATION = new ForbiddenFilter<>();
        FILTER_FORBID_EXTENSION =  new ForbiddenFilter<>();
        FILTER_ALLOW_ALL_RELATIONS = new ForbiddenFilter<>();
        FILTER_ALLOW_ALL_CLASSES = new ForbiddenFilter<>();

        FILTER_FORBID_ANONIMOUS = new ForbiddenRexegFilter();
        FILTER_FORBID_PRIMITIVES = new ForbiddenRexegFilter<>();
        FILTER_FORBID_BASE_CLASSES = new ForbiddenRexegFilter<>();

        FILTER_CHAIN_RELATION_STANDARD = new ChainFilter<>();
        FILTER_CHAIN_RELATION_TYPE_STANDARD  = new ChainFilter<>();
        FILTER_CHAIN_CLASSES_STANDARD  = new ChainFilter<>();
        FILTER_CHAIN_RELATION_TYPE_CUSTOM = new ChainFilter<>();
        FILTER_CHAIN_RELATION_CUSTOM = new ChainFilter<>();
        FILTER_CHAIN_CLASSES_CUSTOM = new ChainFilter<>();

        FILTER_RELATION_FORBID_TO_PRIMITIVE = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.TO);
        FILTER_RELATION_FORBID_TO_BASE = new RelationFieldsFilter(
                RelationFieldsFilter.RelationParts.TO,
                new NotifierOnFiltering(NotifierOnFiltering.OnResult.FAILURE, new RejectingBaseInterfaceEvent())
            );
        FILTER_RELATION_FORBID_FROM_BASE = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.FROM);
        FILTER_RELATION_FORBID_FROM_ANONIMOUS = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.FROM);
        FILTER_RELATION_ALLOW_ALL = new RelationFieldsFilter(RelationFieldsFilter.RelationParts.FROM);

        FILTER_FORBID_ENUM_AGGREGATION_LOOP = new PredicateFilter<>(new Predicate<Relation>() {
            @Override
            public boolean apply(@Nullable Relation relation) {
                Class<?> toType = TypesHelper.loadClass(relation.getToType(), Parser.CLASS_LOADER);
                toType = null != toType ? toType : TypesHelper.loadClass(relation.getToType(), null);
                return ! Aggregation.class.equals(relation.getClass())
                        || ! (relation.getFromType().isEnum() && relation.getFromType().equals(toType));
            }
        });

        FILTER_RELATION_FORBID_AGGREGATION_FROM_PRIVATE = new PredicateFilter<>(new Predicate<Relation>() {
            @Override
            public boolean apply(@Nullable Relation relation) {
                return ! Aggregation.class.equals(relation.getClass())
                        || ! Modifier.isPrivate(relation.getFromType().getModifiers());
            }
        });
        addFiltersToMap();
    }

    /**
     * Configure Filters
     */
    static {
        FILTER_FORBID_USES.addItem(Use.class);
        FILTER_FORBID_AGGREGATION.addItem(Aggregation.class);
        FILTER_FORBID_EXTENSION.addItem(Extension.class);
        FILTER_FORBID_ANONIMOUS.addAllowedItem(Pattern.compile(".*\\$\\d.*"));
        FILTER_FORBID_PRIMITIVES.addAllowedItem(Pattern.compile("[^.]"));
        FILTER_FORBID_BASE_CLASSES.addAllowedItem(Pattern.compile("java\\.(lang|io)\\..*"));

        FILTER_RELATION_FORBID_TO_PRIMITIVE.setFilter(FILTER_FORBID_PRIMITIVES);
        FILTER_RELATION_FORBID_TO_BASE.setFilter(FILTER_FORBID_BASE_CLASSES);
        FILTER_RELATION_FORBID_FROM_BASE.setFilter(FILTER_FORBID_BASE_CLASSES);
        FILTER_RELATION_FORBID_FROM_ANONIMOUS.setFilter(FILTER_FORBID_ANONIMOUS);
        FILTER_RELATION_ALLOW_ALL.setFilter(FILTER_ALLOW_ALL_CLASSES);

        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_TO_PRIMITIVE);
        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_TO_BASE);
        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_FROM_BASE);
        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_RELATION_FORBID_FROM_ANONIMOUS);
        FILTER_CHAIN_RELATION_STANDARD.addFilter(FILTER_FORBID_ENUM_AGGREGATION_LOOP);

        FILTER_CHAIN_RELATION_TYPE_STANDARD.addFilter(FILTER_ALLOW_ALL_RELATIONS);

        FILTER_CHAIN_CLASSES_STANDARD.addFilter(FILTER_FORBID_ANONIMOUS);
        FILTER_CHAIN_CLASSES_STANDARD.addFilter(FILTER_FORBID_BASE_CLASSES);
    }

    private static void addFiltersToMap() {
        // RelationTypes Filters
        FILTERS.put("FILTER_FORBID_USES", FILTER_FORBID_USES);
        FILTERS.put("FILTER_FORBID_AGGREGATION", FILTER_FORBID_AGGREGATION);
        FILTERS.put("FILTER_FORBID_EXTENSION", FILTER_FORBID_EXTENSION);
        FILTERS.put("FILTER_ALLOW_ALL_RELATIONS", FILTER_ALLOW_ALL_RELATIONS);

        // Classes Filters
        FILTERS.put("FILTER_ALLOW_ALL_CLASSES", FILTER_ALLOW_ALL_CLASSES);
        FILTERS.put("FILTER_FORBID_ANONIMOUS", FILTER_FORBID_ANONIMOUS);
        FILTERS.put("FILTER_FORBID_PRIMITIVES", FILTER_FORBID_PRIMITIVES);
        FILTERS.put("FILTER_FORBID_BASE_CLASSES", FILTER_FORBID_BASE_CLASSES);

        // Relations Filters
        FILTERS.put("FILTER_RELATION_FORBID_TO_PRIMITIVE", FILTER_RELATION_FORBID_TO_PRIMITIVE);
        FILTERS.put("FILTER_RELATION_FORBID_TO_BASE", FILTER_RELATION_FORBID_TO_BASE);
        FILTERS.put("FILTER_RELATION_FORBID_FROM_ANONIMOUS", FILTER_RELATION_FORBID_FROM_ANONIMOUS);
        FILTERS.put("FILTER_RELATION_ALLOW_ALL", FILTER_RELATION_ALLOW_ALL);

        FILTERS.put("FILTER_FORBID_ENUM_AGGREGATION_LOOP", FILTER_FORBID_ENUM_AGGREGATION_LOOP);
        FILTERS.put("FILTER_RELATION_FORBID_AGGREGATION_FROM_PRIVATE", FILTER_RELATION_FORBID_AGGREGATION_FROM_PRIVATE);

        // Chain Filters
        FILTERS.put("FILTER_CHAIN_RELATION_TYPE_STANDARD", FILTER_CHAIN_RELATION_TYPE_STANDARD);
        FILTERS.put("FILTER_CHAIN_RELATION_STANDARD", FILTER_CHAIN_RELATION_STANDARD);
        FILTERS.put("FILTER_CHAIN_CLASSES_STANDARD", FILTER_CHAIN_CLASSES_STANDARD);

        FILTERS.put(CHAIN_RELATION_TYPE_CUSTOM_NAME, FILTER_CHAIN_RELATION_TYPE_CUSTOM);
        FILTERS.put(CHAIN_RELATIONS_CUSTOM_NAME, FILTER_CHAIN_RELATION_CUSTOM);
        FILTERS.put(CHAIN_CLASSES_CUSTOM_NAME, FILTER_CHAIN_CLASSES_CUSTOM);
    }
}
