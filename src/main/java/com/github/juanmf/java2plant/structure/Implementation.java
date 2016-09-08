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
package com.github.juanmf.java2plant.structure;

import com.github.juanmf.java2plant.util.TypesHelper;

/**
 * @author juanmf@gmail.com
 */
public class Implementation extends Extension {
    public static final String RELATION_TYPE_IMPLEMENTATION = " ..up|> ";
    public static final String RELATION_TYPE_LOLLIPOP = " -() ";

    public Implementation(Class<?> from, String to) {
        super(from, to);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getFromType().getName(), RELATION_TYPE_IMPLEMENTATION, getToType());
    }

    public String asLollipop() {
        return String.format("\"%s\" %s %s", getFromType().getName(), RELATION_TYPE_LOLLIPOP,
                TypesHelper.getSimpleName(getToType()));
    }
}
