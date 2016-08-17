package com.github.juanmf.java2plant.render.filters;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @param <C> the item type that will be filtered
 *
 * @author juanmf@gmail.com
 */
public class AllowedRexegFilter<C extends Class<?>> implements Filter<C> {
    
	protected Set<Pattern> allowedPatterns = new HashSet<>();
	
    public void addForbiddenItem(Pattern pattern) {
    	allowedPatterns.add(pattern);
    }

    public boolean removeForbiddenItem(Pattern pattern) {
    	return allowedPatterns.remove(pattern);
    }

	@Override
	public boolean satisfy(C item) {
		for (Pattern p : allowedPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return true;
			}
		}
		return false;
	}
}
