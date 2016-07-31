package com.github.juanmf.java2plant.render;

import com.github.juanmf.java2plant.structure.Relation;

import java.util.Set;

/**
 * @author juanmf@gmail.com
 */
public class PlantRenderer {
    private final Set<String> types;
    private final Set<Relation> relations;
    private final Filter filter;

    public PlantRenderer(Set<String> types, Set<Relation> relations) {
        this(types, relations, Filters.FILTER_ALLOW_ALL);
    }

    public PlantRenderer(Set<String> types, Set<Relation> relations, Filter filter) {
        this.types = types;
        this.relations = relations;
        this.filter = filter;

    }

    /**
     * Render full contents
     * <pre>
     *   * Classes
     *   * Relations
     * </pre>
     *
     * @return palntUML src code
     */
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("' Created by juanmf@gmail.com\n\n");
        sb.append("' Participants \n\n");
        addClasses(sb);
        sb.append("\n' Relations \n\n");
        addRelations(sb);
        sb.append("@enduml\n");

        return sb.toString();
    }

    /**
     * Basic Relations renderer, no filtering used.
     *
     * @param sb
     */
    protected void addRelations(StringBuilder sb) {
        for (Relation r : relations) {
            if (filter.isForbidenRelation(r.getClass())) {
                continue;
            }
            sb.append(r.toString()).append("\n");
        }
    }

    /**
     * Basic Participants renderer, no filtering used.
     *
     * @param sb
     */
    protected void addClasses(StringBuilder sb) {
        try {
            for (String c : types) {
                Class<?> aClass = Class.forName(c);
                sb.append(aClass.toString()).append("\n");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
