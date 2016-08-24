package com.github.juanmf.java2plant.render.event;

import com.github.juanmf.java2plant.render.PlantRenderer;
import com.github.juanmf.java2plant.structure.Implementation;

/**
 * Event used to denote a filter rejecting an interface, we can still choose to drag a lollipop interface
 *
 * @author juanmf@gmail.com
 */
public class RejectingBaseInterfaceEvent<T> implements RenderEvent<T> {
    /**
     * The StringBuilder {@link PlantRenderer#render()} is using to writ the plant UML script.
     */
    private StringBuilder sb;

    /**
     * The Relation Object that made a filter fire this event
     * Should happen if the filter is configured to fire an event when rejecting a Base Interface
     */
    private Implementation implementation;

    public void setFilteringObject(T filteringObject) throws ClassCastException {
        this.implementation = (Implementation) filteringObject;
    }

    @Override
    public StringBuilder getScriptStringBuilder() {
        return sb;
    }

    @Override
    public void setScriptStringBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public T getFilteringObject() {
        return (T) implementation;
    }
}
