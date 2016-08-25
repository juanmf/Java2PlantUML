package com.github.juanmf.java2plant.render.filters;

/**
 * @author juanmf@gmail.com
 */
public abstract class NotifyingFilter<T> implements Filter<T> {
    protected final NotifierOnFiltering<T> notifier;

    public NotifyingFilter() {
        this(new NotifierOnFiltering<T>());
    }

    public NotifyingFilter(NotifierOnFiltering<T> notifier) {
        this.notifier = notifier;
    }

    @Override
    public boolean satisfy(T item, StringBuilder sb) {
        return notifier.getResultAndNotify(doSatisfy(item), item, sb);
    }

    protected abstract boolean doSatisfy(T item);
}
