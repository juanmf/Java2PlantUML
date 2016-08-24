package com.github.juanmf.java2plant.render.filters;

import com.google.common.base.Predicate;

/**
 * @author juanmf@gmail.com
 */
public class PredicateFilter<T> implements Filter<T> {

    protected final Predicate<T> predicate;
    private final NotifierOnFiltering<T> notifier;

    public PredicateFilter(Predicate<T> p) {
        this(p, new NotifierOnFiltering<T>());
    }

    public PredicateFilter(Predicate<T> predicate, NotifierOnFiltering<T> notifier) {
        this.notifier = notifier;
        this.predicate = predicate;
    }

    @Override
    public boolean satisfy(T item, StringBuilder sb) {
        return notifier.getResultAndNotify(predicate.apply(item), item, sb);
    }
}
