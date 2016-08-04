package com.github.juanmf.java2plant.render.filters;

import java.util.List;

public interface Filter<T> {
	boolean satisfy(T item);

}
