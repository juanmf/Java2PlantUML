package com.github.juanmf.java2plant.render.event;

/**
 * @author juanmf@gmail.com
 */
public interface RenderEvent<T> {
    StringBuilder getScriptStringBuilder();
    void setScriptStringBuilder(StringBuilder sb);
    void setFilteringObject(T filteringObject);
    T getFilteringObject();
}
