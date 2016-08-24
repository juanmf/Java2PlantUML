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

	protected final NotifierOnFiltering<C> notifier;
	protected Set<Pattern> allowedPatterns = new HashSet<>();

	public AllowedRexegFilter() {
		this(new NotifierOnFiltering<C>());
	}

	public AllowedRexegFilter(NotifierOnFiltering<C> notifier) {
		this.notifier = notifier;
	}

	public void addAllowedItem(Pattern pattern) {
    	allowedPatterns.add(pattern);
    }

    public boolean removeAllowedItem(Pattern pattern) {
    	return allowedPatterns.remove(pattern);
    }

	@Override
	public boolean satisfy(C item, StringBuilder sb) {
		return notifier.getResultAndNotify(doSatisfy(item), item, sb);

	}

	private boolean doSatisfy(C item) {
		for (Pattern p : allowedPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return true;
			}
		}
		return false;
	}
}
