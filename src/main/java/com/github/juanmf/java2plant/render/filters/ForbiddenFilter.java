package com.github.juanmf.java2plant.render.filters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juanmf@gmail.com
 */
public class ForbiddenFilter<T> extends AllowedFilter<T> {

	@Override
	public boolean satisfy(T item) {
		return ! allowedItems.contains(item);
	}
}
