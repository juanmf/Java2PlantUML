/*
 *  Copyright 2016 Juan Manuel Fernandez
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.juanmf.java2plant.util;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author juanmf@gmail.com
 */
public class TypesHelper {
    private static final Logger logger = Logger.getLogger("Main");
    private static final String REGEX_FOR_PACKAGE = "((([ice])(nterface|lass|num))? ?([\\w\\[][_\\w\\d\\$]+\\.)+)";
    private static final Map<String, Class<?>> BOXING_TYPES_FOR_PRIMITIVES = ImmutableMap.<String, Class<?>>builder()
            .put("boolean", Boolean.class)
            .put("byte", Byte.class)
            .put("short", Short.class)
            .put("int", Integer.class)
            .put("long" , Long.class)
            .put("float" , Float.class)
            .put("double" , Double.class)
            .put("char" , Character.class)
            .build();

    /**
     * Should return a decent short version of the FQCN given:
     *
     * <code>
     *     class java.lang.String -> c String
     *     class java.lang.Class<? extends java.net.URLClassLoader> -> c Class<? extends URLClassLoader>
     *     interface java.util.Collection<java.lang.Class<?>> -> i Collection<Class<?>>
     * </code>
     *
     * @param fqcn
     * @return
     */
    public static String getSimpleName(String fqcn) {
        return fqcn.replaceAll(REGEX_FOR_PACKAGE, "$3 ");
    }

    public static Class<?> loadClass(String type, ClassLoader classLoader) {
        try {
            if (isPackage(type)) {
                return null;
            }
            return BOXING_TYPES_FOR_PRIMITIVES.containsKey(type)
                    ? BOXING_TYPES_FOR_PRIMITIVES.get(type)
                    : Class.forName(type, true, classLoader);
        } catch (ClassNotFoundException|NoClassDefFoundError|ExceptionInInitializerError|UnsatisfiedLinkError e) {

            ClassLoader cl = ClassLoader.getSystemClassLoader();
            if (cl != classLoader) {
                return loadClass(type, ClassLoader.getSystemClassLoader());
            } else {
                logger.log(Level.WARNING, String.format(
                        "Issues loading type %s. \n  Throwed: %s: %s. \n  With Loader: %s", type, e.getClass().getName(),
                        e.getMessage(), null == classLoader ? "null" : classLoader.getClass().getName()
                    ), e);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format(
                    "Unhandled Exception while loading %s!! %s: %s", type, e.getClass().getName(), e.getMessage()), e
                );
            throw e;
        }
        return null;
    }

    private static boolean isPackage(String type) {
        return Package.getPackage(type) != null;
    }

    /**
     * Splits the packages list into a List of individual relevant packages or Classes.
     * Expects a comma separated list of packages/Calsses names.
     *
     * @param packages
     * @return The list of relevant packages or Classes
     */
    public static List<String> splitPackages(String packages) {
        return Arrays.asList(packages.trim().split("\\s*,\\s*"));
    }
}
