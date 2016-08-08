package com.github.juanmf.java2plant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.juanmf.java2plant.util.TypesHelper;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.github.juanmf.java2plant.render.PlantRenderer;
import com.github.juanmf.java2plant.render.filters.Filter;
import com.github.juanmf.java2plant.structure.Aggregation;
import com.github.juanmf.java2plant.structure.Extension;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.structure.Use;

/**
 * Iterates over all types available at runtime, under given package, creating:
 * <pre>
 *       Implementations: for Type implemented interfaces
 *       Extensions: for Type extended class
 *       Aggregations: for non private Types declared in Type, taking care of Collection<ActualType> situations
 *       Uses: for dependencies created by non private methods and constructors' parameters.
 * </pre>
 *
 * @author juanmf@gmail.com
 */

public class Parser {
    public static ClassLoader CLASS_LOADER = null;

    /**
     * Parse the given package recursively, then iterates over found types to fetch their relations.
     *
     * @param packageToPase The root package to be parsed.
     *
     * @return PlantUML src code of a Collaboration Diagram for the types found in package and all
     * related Types.
     */
    public static String parse(String packageToPase, Filter<Class<? extends Relation>> relationTypeFilter,
                               Filter<Class<?>> classesFilter, Filter<Relation> relationsFilter) throws ClassNotFoundException
    {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        return parse(packageToPase, relationTypeFilter, classesFilter, classLoadersList, relationsFilter);
    }

    public static String parse(String packageToPase, Filter<Class<? extends Relation>> relationTypeFilter,
                               Filter<Class<?>> classesFilter, ClassLoader classLoader, Filter<Relation> relationsFilter)
    {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(classLoader);
        return parse(packageToPase, relationTypeFilter, classesFilter, classLoadersList, relationsFilter);
    }

    public static String parse(String packageToPase, Filter<Class<? extends Relation>> relationTypeFilter,
                               Filter<Class<?>> classesFilter, List<ClassLoader> classLoadersList,
                               Filter<Relation> relationsFilter)
    {
        Set<Relation> relations = new HashSet<Relation>();
        Set<Class<?>> classes = getTypes(packageToPase, classLoadersList);
        for (Class<?> aClass : classes) {
            addFromTypeRelations(relations, aClass);
        }
        return new PlantRenderer(classes, relations, relationTypeFilter, classesFilter, relationsFilter).render();
    }

    private static Set<Class<?>> getTypes(String packageToPase, List<ClassLoader> classLoadersList) {
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Collection<URL> urls = ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]));
        CLASS_LOADER = new URLClassLoader(urls.toArray(new URL[0]));
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* exclude Object.class */), new ResourcesScanner())
                .setUrls(urls)
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageToPase))));

        Set<String> types = reflections.getAllTypes();
        Set<Class<?>> classes = new HashSet<>();
        for (String type: types) {
            try {
                classes.add(Class.forName(type, true, CLASS_LOADER));
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException: " + e.getMessage());
                continue;
            }
        }
        return classes;
    }

    /**
     * For the given type, adds to relations:
     * <pre>
     *       Implementations: for Type implemented interfaces
     *       Extensions: for Type extended class
     *       Aggregations: for non private Types declared in Type, taking care of Collection<ActualType> situations
     *       Uses: for dependencies created by non private methods and constructors' parameters.
     * </pre>
     *
     * @param relations A Set to add found relations to.
     * @param fromType  The Type originating the relation.
     */
    protected static void addFromTypeRelations(Set<Relation> relations, Class<?> fromType) {
        addImplementations(relations, fromType);
        addExtensions(relations, fromType);
        addAggregations(relations, fromType);
        addUses(relations, fromType);
    }

    protected static void addImplementations(Set<Relation> relations, Class<?> fromType) {
        Class<?>[] interfaces = fromType.getInterfaces();
        for (Class<?> i : interfaces) {
            Relation anImplements = new Extension(fromType, i.getName());
            relations.add(anImplements);
        }
    }

    protected static void addExtensions(Set<Relation> relations, Class<?> fromType) {
        Class<?> superclass = fromType.getSuperclass();
        if (null == superclass || Object.class.equals(superclass)) {
            return;
        }
        Relation extension = new Extension(fromType, superclass.getName());
        relations.add(extension);
    }

    protected static void addAggregations(Set<Relation> relations, Class<?> fromType) {
        Field[] declaredFields = fromType.getDeclaredFields();
        for (Field f : declaredFields) {
            if (! Modifier.isPrivate(f.getModifiers())) {
                addAggregation(relations, fromType, f);
            }
        }
    }

    protected static void addUses(Set<Relation> relations, Class<?> fromType) {
        Method[] methods = fromType.getDeclaredMethods();
        for (Method m: methods) {
            if (! Modifier.isPrivate(m.getModifiers())) {
                addMethodUses(relations, fromType, m);
            }
        }
        Constructor<?>[] constructors = fromType.getDeclaredConstructors();
        for (Constructor<?> c: constructors) {
            if (! Modifier.isPrivate(c.getModifiers())) {
                addConstructorUses(relations, fromType, c);
            }
        }
    }

    protected static void addConstructorUses(Set<Relation> relations, Class<?> fromType, Constructor<?> c) {
        Class<?>[] parameterTypes = c.getParameterTypes();
        Type[] genericParameterTypes = c.getGenericParameterTypes();
        for(int i = 0; i < parameterTypes.length; i++) {
            addConstructorUse(relations, fromType, parameterTypes[i], genericParameterTypes[i], c);
        }
    }

    protected static void addMethodUses(Set<Relation> relations, Class<?> fromType, Method m) {
        Class<?>[] parameterTypes = m.getParameterTypes();
        Type[] genericParameterTypes = m.getGenericParameterTypes();
        for(int i = 0; i < parameterTypes.length; i++) {
            addMethodUse(relations, fromType, parameterTypes[i], genericParameterTypes[i], m);
        }
    }

    protected static void addConstructorUse(Set<Relation> relations, Class<?> fromType, Class<?> toType, Type fromParameterType, Constructor<?> c) {
        String name = TypesHelper.getSimpleName(c.getName()) + "()";
        addUse(relations, fromType, toType, fromParameterType, c, name);
    }

    protected static void addUse(Set<Relation> relations, Class<?> fromType, Class<?> toType, Type fromParameterType, Member m, String msg) {
        String toName = toType.getName();
        if (isMulti(toType)) {
            if (! toType.isArray()) {
                if (fromParameterType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) fromParameterType;
                    Set<String> typeVars = getTypeParams(pt);
                    for (String t : typeVars) {
                        msg += toName;
                        Relation use = new Use(fromType, t, m, msg);
                        relations.add(use);
                    }
                    return;
                }
            }
            toName = toType.getCanonicalName().replace("[]", "");
            msg += ": []";
        }
        Relation use = new Use(fromType, toName, m, msg);
        relations.add(use);
    }

    protected static void addMethodUse(Set<Relation> relations, Class<?> fromType, Class<?> toType, Type fromParameterType, Method m) {
        String name = TypesHelper.getSimpleName(m.getName()) + "()";
        addUse(relations, fromType, toType, fromParameterType, m, name);
    }

    protected static boolean isMulti(Class<?> delegateType) {
        return delegateType.isArray()
                || Collection.class.isAssignableFrom(delegateType)
                || Map.class.isAssignableFrom(delegateType);
    }

    protected static void addAggregation(Set<Relation> relations, Class<?> fromType, Field f) {
        Class<?> delegateType = f.getType();
        String varName = f.getName();
        String toCardinal = "1";
        String toName = delegateType.getName();
        if (isMulti(delegateType)) {
            toCardinal = "*";
            if (! delegateType.isArray()) {
                Set<String> typeVars = getTypeParams(f);
                for (String type : typeVars) {
                    Relation aggregation = new Aggregation(
                            fromType, type, f, toCardinal, varName + ": " + delegateType.getName());
                    relations.add(aggregation);
                }
                return;
            }
            toName = delegateType.getCanonicalName().replace("[]", "");
        }
        Relation aggregation = new Aggregation(fromType, toName, f, toCardinal, varName);
        relations.add(aggregation);
    }

    protected static Set<String> getTypeParams(Field f) {
        Type tp = f.getGenericType();
        if (tp instanceof ParameterizedType) {
            Set<String> typeVars = getTypeParams((ParameterizedType) tp);
           return typeVars;
        }
        return Collections.emptySet();
    }

    protected static Set<String> getTypeParams(ParameterizedType f) {
        Set<String> typeVars = new HashSet<>();
        Type[] actualTypeArguments = f.getActualTypeArguments();
        for (Type t: actualTypeArguments) {
            typeVars.add(t.toString().replace("class ", ""));
        }
        return typeVars;
    }
}
