package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author juanmf@gmail.com
 */
public class ForbiddenRexegFilter<T extends Class<?>> implements Filter<T> {
    
	private Set<Pattern> forbiddenPatterns = new HashSet<>();
	
    public void addForbiddenItem(Pattern pattern) {
    	forbiddenPatterns.add(pattern);
    }

    public boolean removeForbiddenItem(Pattern pattern) {
    	return forbiddenPatterns.remove(pattern);
    }

    @Override
	public boolean satisfy(T item) {
		for (Pattern p : forbiddenPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return false;
			}
		}
		return true;
	}
}
