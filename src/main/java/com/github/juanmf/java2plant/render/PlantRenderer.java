package com.github.juanmf.java2plant.render;

import com.github.juanmf.java2plant.render.filters.ChainFilter;
import com.github.juanmf.java2plant.render.filters.Filter;
import com.github.juanmf.java2plant.render.filters.Filters;
import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.structure.Use;
import com.github.juanmf.java2plant.util.TypesHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author juanmf@gmail.com
 */
public class PlantRenderer {
    private final Set<Class<?>> types;
    private final Set<Relation> relations;
    private final Filter<Class<?>> classesFilter;
    private final Filter<Class<? extends Relation>> relationsTypeFilter;
    private Filter<Relation> relationsFilter;
    private final Set<Pattern> toTypesToShowAsMember;
    private static final Map<Class<? extends Relation>, MemberPrinter> memberPrinters = new HashMap<>();

    static {
        memberPrinters.put(Use.class, new MethodPrinter());
        memberPrinters.put(Aggregation.class, new FieldPrinter());
        memberPrinters.put(Extension.class, new NullPrinter());
    }


    public PlantRenderer(Set<Class<?>> types, Set<Relation> relations) {
        this(types, relations, Filters.FILTER_ALLOW_ALL_RELATIONS, Filters.FILTER_ALLOW_ALL_CLASSES, Filters.FILTER_CHAIN_RELATION_STANDARD);
    }

    public PlantRenderer(Set<Class<?>> types, Set<Relation> relations, Filter<Class<? extends Relation>> relationTypeFilter,
                         Filter<Class<?>> classesFilter, Filter<Relation> relationsFilter)
    {
        this.types = types;
        this.relations = relations;
        this.relationsTypeFilter = relationTypeFilter;
        this.classesFilter = classesFilter;
        toTypesToShowAsMember = new HashSet<>();
        toTypesToShowAsMember.add(Pattern.compile("^java.lang.*"));
        toTypesToShowAsMember.add(Pattern.compile("^[^\\$]*"));
        this.relationsFilter = relationsFilter;
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
            if (! relationsTypeFilter.satisfy(r.getClass())
                    || ! relationsFilter.satisfy(r)) {
                continue;
            }
            addRelation(sb, r);
        }
    }

    private void addRelation(StringBuilder sb, Relation r) {
        if (r instanceof Use && isToTypeInAggregations(r)) {
            return;
        }
        sb.append(r.toString()).append("\n");
    }

    private boolean isToTypeInAggregations(Relation r) {
        try {
            Class<?> toType = Class.forName(r.getToType(), true, Parser.CLASS_LOADER);
            Class<?> origin = r.getFromType();
            for (Field f: origin.getDeclaredFields()) {
                // TODO: There migth be cases where toType is a generic Type param and this won't do well e.g.
                // Collection<Type>
                if (f.getType().equals(toType)) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
        }
        return false;
    }

    /**
     * Basic Participants renderer, no filtering used.
     *
     * @param sb
     */
    protected void addClasses(StringBuilder sb) {
        for (Class<?> c : types) {
        	if (! classesFilter.satisfy(c)){
                System.out.println("Not adding class " + c);
                continue;
            }
            addClass(sb, c);
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
            if (relation.getFromType().equals(aClass)
                    && matches(relation.getToType(), toTypesToShowAsMember)) {
                System.out.println(String.format("%s has a relation to %s to be shown as member", aClass.getName(), relation.getToType()));
                memberPrinters.get(relation.getClass()).addMember(relation, fields, methods);
                relation.setPrintedAsMember(true);
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
            Member m = r.getOriginatingMember();
            if (m.isSynthetic()) {
                System.out.println("skiping synthetic" + m);
                return;
            }

            String msg = String.format("%s %s : %s", Modifiers.forModifier(m.getModifiers()), m.getName(),
                    TypesHelper.getSimpleName(r.getToType()));
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
            Member m = r.getOriginatingMember();
            if (m.isSynthetic()) {
                System.out.println("skiping synthetic" + m);
                return;
            }
            String name = TypesHelper.getSimpleName(m.getName());
            String modif = Modifiers.forModifier(m.getModifiers()).toString();
            String returnType = (m instanceof Method)
                    ? " : " + TypesHelper.getSimpleName(((Method) m).getReturnType().getName())
                    : "";
            StringBuilder params = new StringBuilder();
            List<? extends TypeVariable<?>> paramClasses = Arrays.asList(
                    ((GenericDeclaration) m).getTypeParameters());
            Iterator<? extends TypeVariable<?>> it = paramClasses.iterator();
            while (it.hasNext()) {
                TypeVariable<?> c = it.next();
                params.append(TypesHelper.getSimpleName(c.getName()));
                if (it.hasNext()) {
                    params.append(", ");
                }
            }

            if (m instanceof Constructor && 0 == params.length()
                    && Modifiers.PUBLIC.equals(Modifiers.forModifier(m.getModifiers()))) {
                return;
            }
            String msg = String.format("%s %s(%s) %s", modif, name, params.toString(), returnType);
            methods.add(msg);
        }
    }

    private enum Modifiers {
        PUBLIC("+"),
        PROTECTED("#"),
        PRIVATE("-"),
        DEFAULT("~");

        String prefix;

        Modifiers(String prefix) {
            this.prefix = prefix;
        }

        public static Modifiers forModifier (int memberModifier) {
            Modifiers m = null;
            if (Modifier.isPrivate(memberModifier)) {
                m = PRIVATE;
            }
            if (Modifier.isProtected(memberModifier)) {
                m = PROTECTED;
            }
            if (Modifier.isPublic(memberModifier)) {
                m = PUBLIC;
            }
            if (null == m) {
                m = DEFAULT;
            }
            return m;
        }

        @Override
        public String toString() {
            return prefix + " ";
        }
    }
}
