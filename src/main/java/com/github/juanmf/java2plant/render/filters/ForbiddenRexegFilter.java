package com.github.juanmf.java2plant.render.filters;

import java.util.regex.Pattern;

/**
 * @param <C> the item type that will be filtered
 *
 * @author juanmf@gmail.com
 */
public class ForbiddenRexegFilter<C extends Class<?>> extends AllowedRexegFilter<C> {

	public ForbiddenRexegFilter() {
		super();
	}

	public ForbiddenRexegFilter(NotifierOnFiltering<C> notifier) {
		super(notifier);
	}

	/**
	 * {@link #allowedPatterns} actually should hold forbidden items
	 *
	 * @param item a Class to match against.
	 *
	 * @param sb
	 * @return false if {@link #allowedPatterns} contains a pattern that matches item
     */
    @Override
	public boolean satisfy(C item, StringBuilder sb) {
		return notifier.getResultAndNotify(doSatisfy(item), item, sb);
	}

	public boolean doSatisfy(C item) {
		for (Pattern p : allowedPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return false;
			}
		}
		return true;
	}
}
