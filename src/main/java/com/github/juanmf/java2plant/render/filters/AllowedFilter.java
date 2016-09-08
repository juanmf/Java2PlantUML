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

/**
 * @author juanmf@gmail.com
 */
public class AllowedFilter<T> extends NotifyingFilter<T> {

    protected Set<T> allowedItems = new HashSet<>();

    public AllowedFilter() {
        super();
    }

    public AllowedFilter(NotifierOnFiltering<T> notifier) {
        super(notifier);
    }

    public void addItem(T item) {
        allowedItems.add(item);
    }

    public boolean removeItem(T item) {
        return allowedItems.remove(item);
    }

    @Override
    protected boolean doSatisfy(T item) {
        return allowedItems.contains(item);
    }
}
