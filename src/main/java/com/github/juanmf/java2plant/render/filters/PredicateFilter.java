package com.github.juanmf.java2plant.render.filters;

import com.google.common.base.Predicate;

/**
 * @author juanmf@gmail.com
 */
public class PredicateFilter<T> implements Filter<T> {

    protected final Predicate<T> predicate;

    public PredicateFilter(Predicate<T> p) {
        this.predicate = p;
    }

    @Override
    public boolean satisfy(T item) {
        return predicate.apply(item);
    }
}
