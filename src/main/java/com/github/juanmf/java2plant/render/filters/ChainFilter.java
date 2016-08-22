package com.github.juanmf.java2plant.render.filters;

import java.util.HashSet;
import java.util.Set;

/**
 * @author juanmf@gmail.com
 */
public class ChainFilter<T> implements Filter<T> {

    Set<Filter<T>> filters = new HashSet<>();

    public boolean addFilter(Filter<T> filter) {
        return filters.add(filter);
    }

    public boolean removeFilter(Filter<T> filter) {
        return filters.remove(filter);
    }

    @Override
    public boolean satisfy(T item) {
        for (Filter<T> f : filters) {
            if (! f.satisfy(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("ChainFilter %s with: {%s}", super.toString(), filters.toString());
    }
}
