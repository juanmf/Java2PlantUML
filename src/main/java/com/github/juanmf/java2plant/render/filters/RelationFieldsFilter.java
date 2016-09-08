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


import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.structure.Relation;
import com.github.juanmf.java2plant.util.TypesHelper;

/**
 * @author juanmf@gmail.com
 */
public class RelationFieldsFilter<T extends Relation> implements Filter<T> {

    private final NotifierOnFiltering<T> notifier;
    private Filter<Class<?>> filter = Filters.FILTER_ALLOW_ALL_CLASSES;
    private final RelationParts part;

    public RelationFieldsFilter(RelationParts part) {
        this(part, new NotifierOnFiltering<T>());
    }

    public RelationFieldsFilter(RelationParts part, NotifierOnFiltering<T> notifier) {
        this.part = part;
        this.notifier = notifier;
    }

    public void setFilter(Filter<Class<?>> filter) {
        this.filter = filter;
    }

    @Override
    public boolean satisfy(T item, StringBuilder sb) {
        Class<?> aClass = part.getType(item);
        if (null == aClass) {
            return false;
        }
        return notifier.getResultAndNotify(filter.satisfy(aClass, sb), item, sb);
    }

    enum RelationParts {
        TO(new PartExtractor(){
            @Override
            public Class<?> extract(Relation relation) {
                String toType = relation.getToType();
                Class<?> aClass = TypesHelper.loadClass(toType, Parser.CLASS_LOADER);
                return aClass;
            }
        }),
        FROM(new PartExtractor(){
            @Override
            public Class<?> extract(Relation relation) {
                return relation.getFromType();
            }
        });

        private final PartExtractor partExtractor;

        <T extends Relation> RelationParts(PartExtractor extractor) {
            this.partExtractor = extractor;
        }

        public Class<?> getType(Relation  relation) {
            return partExtractor.extract(relation);
        }
        private interface PartExtractor {
            Class<?> extract(Relation relation);
        }
    }
}
