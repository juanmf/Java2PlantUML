package com.github.juanmf.java2plant.render.filters;


import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.structure.Relation;

/**
 * @author juanmf@gmail.com
 */
public class RelationFieldsFilter implements Filter<Relation> {

    private Filter<Class<?>> filter = Filters.FILTER_ALLOW_ALL_CLASSES;
    private final RelationParts part;

    public RelationFieldsFilter(RelationParts part) {
        this.part = part;
    }

    public void setFilter(Filter<Class<?>> filter) {
        this.filter = filter;
    }

    @Override
    public boolean satisfy(Relation item) {
        if (RelationParts.TO.equals(part)) {
            return apply(item.getToType());
        }
        if (RelationParts.FROM.equals(part)) {
            return apply(item.getFromType());
        }
        return false;
    }

    private boolean apply(String toType) {
        // TODO: EVALUATE REFACTOR TO cLASS FOR TOTYPE
        try {
            return apply(Class.forName(toType, true, Parser.CLASS_LOADER));
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find class " + toType);
        }
        return false;
    }

    private boolean apply(Class<?> fromType) {
        return filter.satisfy(fromType);
    }

    enum RelationParts {
        TO,
        FROM;
    }
}
