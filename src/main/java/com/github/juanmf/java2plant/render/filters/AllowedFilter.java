package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author juanmf@gmail.com
 */
public class AllowedFilter<T> implements Filter<T> {

	protected Set<T> allowedItems = new HashSet<>();

    public void addItem(T item) {
    	allowedItems.add(item);
    }

    public boolean removeItem(T item) {
    	return allowedItems.remove(item);
    }

	@Override
	public boolean satisfy(T item) {
		return allowedItems.contains(item);
	}
}
