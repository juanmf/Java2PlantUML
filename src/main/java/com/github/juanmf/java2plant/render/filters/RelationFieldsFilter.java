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
                aClass = null == aClass ? TypesHelper.loadClass(toType, null) : aClass;
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
