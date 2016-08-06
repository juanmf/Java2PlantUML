package com.github.juanmf.java2plant.render.filters;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author juanmf@gmail.com
 */
public class AllowedRexegFilter<T extends Pattern, C extends Class<?>> implements Filter<T> {
    
	private Set<T> allowedPatterns = new HashSet<T>();
	
    public void addForbiddenItem(T pattern) {
    	allowedPatterns.add(pattern);
    }

    public boolean removeForbiddenItem(T pattern) {
    	return allowedPatterns.remove(pattern);
    }

	public boolean satisfy(C item) {
		for (Pattern p : allowedPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean satisfy(T item) {
		throw new UnsupportedOperationException("I accept patterns to forbid class, Class<?> expected for satisfy");
	}
}
