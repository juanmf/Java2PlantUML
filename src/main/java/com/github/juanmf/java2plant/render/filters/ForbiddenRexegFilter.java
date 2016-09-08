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
package com.github.juanmf.java2plant.render.filters;

import java.util.regex.Pattern;

/**
 * @param <C> the item type that will be filtered
 *
 * @author juanmf@gmail.com
 */
public class ForbiddenRexegFilter<C extends Class<?>> extends AllowedRexegFilter<C> {

    public ForbiddenRexegFilter() {
        super();
    }

    public ForbiddenRexegFilter(NotifierOnFiltering<C> notifier) {
        super(notifier);
    }

    /**
     * {@link #allowedPatterns} actually should hold forbidden items
     *
     * @param item a Class to match against.
     *
     * @return false if {@link #allowedPatterns} contains a pattern that matches item
     */
    @Override
    protected boolean doSatisfy(C item) {
        for (Pattern p : allowedPatterns) {
            if (p.matcher(item.getName()).matches()) {
                return false;
            }
        }
        return true;
    }
}
