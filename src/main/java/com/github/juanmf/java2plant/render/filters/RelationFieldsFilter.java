package com.github.juanmf.java2plant.render.filters;


import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.util.TypesHelper;

/**
 * @author juanmf@gmail.com
 */
public class RelationFieldsFilter<T extends Relation> implements Filter<T> {

    private Filter<Class<?>> filter = Filters.FILTER_ALLOW_ALL_CLASSES;
    private final RelationParts part;
    private final NotifierOnFiltering<T> notifier;

    public RelationFieldsFilter(RelationParts part) {
        this(part, new NotifierOnFiltering<T>());
    }

    public RelationFieldsFilter(RelationParts part, NotifierOnFiltering<T> notifier) {
        this.part = part;
        this.notifier = notifier;
    }

    public void setFilter(Filter<Class<?>> filter) {
        this.filter = filter;
    }

    @Override
    public boolean satisfy(T item, StringBuilder sb) {
        if (RelationParts.TO.equals(part)) {
            return apply(item.getToType(), item, sb);
        }
        if (RelationParts.FROM.equals(part)) {
            return apply(item.getFromType(), item, sb);
        }
        throw new IllegalStateException("Relation part is not supported: " + part);
    }

    private boolean apply(String toType,  T item, StringBuilder sb) {
        // TODO: EVALUATE REFACTOR TO cLASS FOR TOTYPE
        Class<?> aClass = TypesHelper.loadClass(toType, Parser.CLASS_LOADER);
        aClass = null == aClass ? TypesHelper.loadClass(toType, null) : aClass;
        if (null != aClass) {
            return apply(aClass, item, sb);
        }
        return false;
    }

    private boolean apply(Class<?> type, T item, StringBuilder sb) {
        return notifier.getResultAndNotify(filter.satisfy(type, sb), item, sb);
    }

    enum RelationParts {
        TO,
        FROM;
    }
}
