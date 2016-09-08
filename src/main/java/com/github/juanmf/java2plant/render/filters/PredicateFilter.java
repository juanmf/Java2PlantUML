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

import com.google.common.base.Predicate;

/**
 * @author juanmf@gmail.com
 */
public class PredicateFilter<T> extends NotifyingFilter<T> {

    protected final Predicate<T> predicate;

    public PredicateFilter(Predicate<T> predicate) {
        super();
        this.predicate = predicate;
    }

    public PredicateFilter(Predicate<T> predicate, NotifierOnFiltering<T> notifier) {
        super(notifier);
        this.predicate = predicate;
    }

    @Override
    protected boolean doSatisfy(T item) {
        return predicate.apply(item);
    }
}
