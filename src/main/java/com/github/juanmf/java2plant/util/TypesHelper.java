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

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

/**
 * @author juanmf@gmail.com
 */
public class TypesHelper {
    private static final String REGEX_FOR_PACKAGE = "((([ice])(nterface|lass|num))? ?([\\w\\[][_\\w\\d\\$]+\\.)+)";

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

    public static Class<?> loadClass(String type, URLClassLoader classLoader) {
        try {
            return Class.forName(type, true, classLoader);
        } catch (ClassNotFoundException|NoClassDefFoundError|ExceptionInInitializerError|UnsatisfiedLinkError e) {
            System.out.println(String.format(
                    "Issues loading type %s. \n  Throwed: %s: %s. \n  With Loader: %s", type, e.getClass().getName(),
                    e.getMessage(), null == classLoader ? "null" : classLoader.getClass().getName()
                ));

            if (null != classLoader) {
                return loadClass(type, null);
            }
        } catch (Exception e) {
            System.out.println(String.format(
                    "Unhandled Exception while loading %s!! %s: %s", type, e.getClass().getName(), e.getMessage())
                );
            throw e;
        }
        return null;
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
