package com.github.juanmf.java2plant.render.filters;

/**
 * @author juanmf@gmail.com
 */
public class ForbiddenFilter<T> extends AllowedFilter<T> {

	public ForbiddenFilter() {
		super();
	}

	public ForbiddenFilter(NotifierOnFiltering<T> notifier) {
		super(notifier);
	}

	@Override
	public boolean satisfy(T item, StringBuilder sb) {
		return ! allowedItems.contains(item);
	}
}
