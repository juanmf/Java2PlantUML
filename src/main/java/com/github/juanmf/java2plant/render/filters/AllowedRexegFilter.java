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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @param <C> the item type that will be filtered
 *
 * @author juanmf@gmail.com
 */
public class AllowedRexegFilter<C extends Class<?>>  extends NotifyingFilter<C> {

    protected Set<Pattern> allowedPatterns = new HashSet<>();

    public AllowedRexegFilter() {
        super();
    }

    public AllowedRexegFilter(NotifierOnFiltering<C> notifier) {
        super(notifier);
    }

    public void addAllowedItem(Pattern pattern) {
        allowedPatterns.add(pattern);
    }

    public boolean removeAllowedItem(Pattern pattern) {
        return allowedPatterns.remove(pattern);
    }

    @Override
    protected boolean doSatisfy(C item) {
        for (Pattern p : allowedPatterns) {
            if (p.matcher(item.getName()).matches()) {
                return true;
            }
        }
        return false;
    }
}
