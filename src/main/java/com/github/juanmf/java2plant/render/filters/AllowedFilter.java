package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author juanmf@gmail.com
 */
public class AllowedFilter<T> implements Filter<T> {

	private Set<T> allowedItems = new HashSet<>();

    public void addForbiddenItem(T relation) {
    	allowedItems.add(relation);
    }

    public boolean removeForbiddenItem(T relation) {
    	return allowedItems.remove(relation);
    }

	@Override
	public boolean satisfy(T item) {
		return allowedItems.contains(item);
	}
}
