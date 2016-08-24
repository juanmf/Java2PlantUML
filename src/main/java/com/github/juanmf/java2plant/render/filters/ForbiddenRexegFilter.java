package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @param <C> the item type that will be filtered
 *
 * @author juanmf@gmail.com
 */
public class ForbiddenRexegFilter<C extends Class<?>> extends AllowedRexegFilter<C> {

	/**
	 * {@link #allowedPatterns} actually should hold forbidden items
	 *
	 * @param item a Class to match against.
	 *
	 * @return false if {@link #allowedPatterns} contains a pattern that matches item
     */
    @Override
	public boolean satisfy(C item) {
		for (Pattern p : allowedPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return false;
			}
		}
		return true;
	}
}
