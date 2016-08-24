package com.github.juanmf.java2plant.render.filters;

import java.util.HashSet;
import java.util.Set;

/**
 * @author juanmf@gmail.com
 */
public class AllowedFilter<T> implements Filter<T> {

	protected final NotifierOnFiltering<T> notifier;
	protected Set<T> allowedItems = new HashSet<>();

	public AllowedFilter() {
		this(new NotifierOnFiltering<T>());
	}

	public AllowedFilter(NotifierOnFiltering<T> notifier) {
		this.notifier = notifier;
	}

	public void addItem(T item) {
    	allowedItems.add(item);
    }

    public boolean removeItem(T item) {
    	return allowedItems.remove(item);
    }

	@Override
	public boolean satisfy(T item, StringBuilder sb) {
		return notifier.getResultAndNotify(allowedItems.contains(item), item, sb);
	}
}
