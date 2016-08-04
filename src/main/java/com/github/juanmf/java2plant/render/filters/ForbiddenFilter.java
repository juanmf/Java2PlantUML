package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.List;

import com.github.juanmf.java2plant.structure.Relation;

/**
 * @author juanmf@gmail.com
 */
public class ForbiddenFilter<T> implements Filter<T> {
    
	private List<T> forbidenItems = new ArrayList<T>();
	
    public void addForbidenItem(T relation) {
    	forbidenItems.add(relation);
    }

    public boolean removeForbidenItem(T relation) {
    	return forbidenItems.remove(relation);
    }
    
	@Override
	public boolean satisfy(T item) {
		return !forbidenItems.contains(item);
	}
}
