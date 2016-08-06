package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juanmf@gmail.com
 */
public class ForbiddenFilter<T> implements Filter<T> {
    
	private List<T> forbiddenItems = new ArrayList<T>();
	
    public void addForbiddenItem(T relation) {
    	forbiddenItems.add(relation);
    }

    public boolean removeForbiddenItem(T relation) {
    	return forbiddenItems.remove(relation);
    }
    
	@Override
	public boolean satisfy(T item) {
		return ! forbiddenItems.contains(item);
	}
}