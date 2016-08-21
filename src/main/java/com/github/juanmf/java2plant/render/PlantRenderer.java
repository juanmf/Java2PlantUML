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
import edu.emory.mathcs.backport.java.util.Collections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
    private static final Map<Class<? extends Member>, MemberPrinter> memberPrinters = new HashMap<>();

    static {
        MethodPrinter mp = new MethodPrinter();
        memberPrinters.put(Field.class, new FieldPrinter());
        memberPrinters.put(Constructor.class, mp);
        memberPrinters.put(Method.class, mp);
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
        sb.append("@startuml\n")
                .append("' Created by juanmf@gmail.com\n\n")
                .append("' Using left to right direction to try a better layout feel free to edit\n")
                .append("left to right direction\n");

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
        ArrayList<Relation> relations = new ArrayList(this.relations);
        Collections.sort(relations, new Comparator<Relation>() {
            @Override
            public int compare(Relation o1, Relation o2) {
                int result = o1.getClass().equals(o2.getClass())
                        ? o1.getFromType().getName().compareTo(o1.getFromType().getName())
                        : o1.getClass().getName().compareTo(o2.getClass().getName());
                return result;
            }
        });
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
        Class<?> toType = TypesHelper.loadClass(r.getToType(), Parser.CLASS_LOADER);
        toType = null == toType ? TypesHelper.loadClass(r.getToType(), null) : toType;
        Class<?> origin = r.getFromType();
        for (Field f: origin.getDeclaredFields()) {
            // TODO: There migth be cases where toType is a generic Type param and this won't do well e.g. Collection<Type>
            if (f.getType().equals(toType)) {
                return true;
            }
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
        String classDeclaration = aClass.isEnum() ? "enum " + aClass.getName() : aClass.toString();
        sb.append(classDeclaration).append(" {\n");
        renderClassMembers(sb, aClass);
        sb.append("\n}\n");
    }

    private void renderClassMembers(StringBuilder sb, Class<?> aClass) {
        List<String> fields = new ArrayList<>();
        List<String> methods = new ArrayList<>();
        List<String> constructors = new ArrayList<>();

        addMembers(aClass.getDeclaredFields(), fields);
        addMembers(aClass.getDeclaredConstructors(), constructors);
        addMembers(aClass.getDeclaredMethods(), methods);

        Collections.sort(fields);
        Collections.sort(methods);
        Collections.sort(constructors);

        for (String field : fields) {
            sb.append(field + "\n");
        }
        sb.append("--\n");
        for (String constructor : constructors) {
            sb.append(constructor + "\n");
        }
        for (String method : methods) {
            sb.append(method + "\n");
        }
    }

    private void addMembers(Member[] declaredMembers, List<String> plantMembers) {
        for (Member m : declaredMembers) {
            memberPrinters.get(m.getClass()).addMember(m, plantMembers);
        }

    }

    interface MemberPrinter {
        void addMember(Member m, List<String> plantMembers);
    }

    static class FieldPrinter implements MemberPrinter {
        @Override
        public void addMember(Member m, List<String> plantMembers) {
            Field f = (Field) m;
            if (f.isSynthetic()) {
                System.out.println("skiping synthetic" + f);
                return;
            }

            String msg = String.format("%s %s : %s", Modifiers.forModifier(f.getModifiers()), f.getName(),
                    TypesHelper.getSimpleName(f.getType().getName()));
            plantMembers.add(msg);
        }
    }

    static class NullPrinter implements MemberPrinter {
        @Override
        public void addMember(Member m, List<String> plantMembers) {
            System.out.println(String.format("skipping member %s.", m));
        }
    }

    /**
     * Used for Constructors or Methods
     */
    static class MethodPrinter implements MemberPrinter {
        @Override
        public void addMember(Member m, List<String> plantMembers) {
            if (m.isSynthetic()) {
                System.out.println("skiping synthetic" + m);
                return;
            }
            String name = TypesHelper.getSimpleName(m.getName());
            String modif = Modifiers.forModifier(m.getModifiers()).toString();
            String returnType = (m instanceof Method)
                    ? " : " + TypesHelper.getSimpleName(((Method) m).getReturnType().getName())
                    : "";
            String params = buildParams(m);
            String msg = String.format("%s %s(%s) %s", modif, name, params, returnType);
            plantMembers.add(msg);
        }

        private String buildParams(Member m) {
            StringBuilder params = new StringBuilder();
            Type[] paramClasses = m instanceof Method
                    ? ((Method) m).getGenericParameterTypes()
                    : ((Constructor) m).getGenericParameterTypes();
            Iterator<? extends Type> it = Arrays.asList(paramClasses).iterator();
            while (it.hasNext()) {
                Type c = it.next();
                params.append(TypesHelper.getSimpleName(c.toString()));
                if (it.hasNext()) {
                    params.append(", ");
                }
            }
            return params.toString();
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
