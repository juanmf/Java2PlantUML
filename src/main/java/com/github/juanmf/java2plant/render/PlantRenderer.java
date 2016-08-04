package com.github.juanmf.java2plant.render;

import com.github.juanmf.java2plant.render.filters.Filter;
import com.github.juanmf.java2plant.render.filters.Filters;
import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.structure.Use;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author juanmf@gmail.com
 */
public class PlantRenderer {
    private final Set<String> types;
    private final Set<Relation> relations;
    private final Filter<String> classesFilter;
    private final Filter<Class<? extends Relation>> relationsFilter;
    private final Set<Pattern> toTypesToShowAsMember;
    private static final Map<Class<? extends Relation>, MemberPrinter> memberPrinters = new HashMap<>();

    static {
        memberPrinters.put(Use.class, new MethodPrinter());
        memberPrinters.put(Aggregation.class, new FieldPrinter());
        memberPrinters.put(Extension.class, new NullPrinter());
    }

    public PlantRenderer(Set<String> types, Set<Relation> relations) {
        this(types, relations, Filters.FILTER_ALLOW_ALL_RELATIONS,Filters.FILTER_ALLOW_ALL_CLASSES);
    }

    public PlantRenderer(Set<String> types, Set<Relation> relations, Filter<Class<? extends Relation>> relationsFilter, Filter<String> classesFilter) {
        this.types = types;
        this.relations = relations;
        this.relationsFilter = relationsFilter;
        this.classesFilter = classesFilter;
        toTypesToShowAsMember = new HashSet<>();
        toTypesToShowAsMember.add(Pattern.compile("^java.lang.*"));
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
        sb.append("' Using left to right direction to try a better layout feel free to edit\n");
        sb.append("left to right direction\n");
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
            if (!relationsFilter.satisfy(r.getClass())) {
                continue;
            }
            if (r.getFromType().contains("$")) {
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
        for (String c : types) {
        	if (!classesFilter.satisfy(c)){
            	continue;
            }
        	try {
                Class<?> aClass = Class.forName(c, true, Parser.CLASS_LOADER);
                addClass(sb, aClass);
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException: " + e.getMessage());
                continue;
            }
        }
    }

    protected void addClass(StringBuilder sb, Class<?> aClass) {
        if (aClass.getName().contains("$")) {
            return;
        }
        sb.append(aClass.toString()).append(" {\n");
        renderClassMembers(sb, aClass);
        sb.append("\n} \n ");
    }

    private void renderClassMembers(StringBuilder sb, Class<?> aClass) {
        Iterator<Relation> ri = relations.iterator();
        Set<String> fields = new HashSet<>();
        Set<String> methods = new HashSet<>();
        while (ri.hasNext()) {
            Relation relation = ri.next();
            if (relation.getFromType().equals(aClass.getName())
                    && matches(relation.getToType(), toTypesToShowAsMember)) {
                System.out.println(String.format("%s has a relation to %s to be shown as member", aClass.getName(), relation.getToType()));
                memberPrinters.get(relation.getClass()).addMember(relation, fields, methods);
                ri.remove();
            }
        }
        for (String field : fields) {
            sb.append(field + "\n");
        }
        sb.append("--\n");
        for (String method : methods) {
            sb.append(method + "\n");
        }
    }

    private boolean matches(String toType, Set<Pattern> toTypesToShowAsAttrs) {
        for (Pattern pattern : toTypesToShowAsAttrs) {
            Matcher m = pattern.matcher(toType);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    interface MemberPrinter {
        void addMember(Relation r, Set<String> fields, Set<String> methods);
    }

    static class FieldPrinter implements MemberPrinter {
        @Override
        public void addMember(Relation r, Set<String> fields, Set<String> methods) {
            String msg = r.getMessage();
            fields.add(msg);
        }
    }

    static class NullPrinter implements MemberPrinter {
        @Override
        public void addMember(Relation r, Set<String> fields, Set<String> methods) {
            System.out.println(String.format("skipping %s to %s relation", r.getFromType(), r.getToType()));
        }
    }

    static class MethodPrinter implements MemberPrinter {
        @Override
        public void addMember(Relation r, Set<String> fields, Set<String> methods) {
            String msg = r.getMessage();
            methods.add(msg);
        }
    }
}
