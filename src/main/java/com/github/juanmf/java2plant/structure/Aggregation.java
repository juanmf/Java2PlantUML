package com.github.juanmf.java2plant.structure;

import java.lang.reflect.Member;

/**
 * @author juanmf@gmail.com
 */
public class Aggregation implements Relation {
    public static final String RELATION_TYPE_AGGREGATION = " o-left- ";
    private final String toFieldName;
    private final Class<?> from;
    private final String to;
    private final String toCardinal;
    private final Member originatingMember;

    public Aggregation(Class<?> from, String to, Member originatingMember, String toCardinal) {
        this(from, to, originatingMember, toCardinal, null);
    }

    public Aggregation(Class<?> from, String to, Member originatingMember, String toCardinal, String toFieldName) {
        this.from = from;
        this.to = to;
        this.toCardinal = toCardinal;
        this.toFieldName = toFieldName;
        this.originatingMember = originatingMember;
    }

    public Class<?> getFromType() {
        return from;
    }

    @Override
    public Member getOriginatingMember() {
        return originatingMember;
    }

    public String getToType() {
        return to;
    }

    public String getRelationType() {
        return RELATION_TYPE_AGGREGATION;
    }

    public String getMessage() {
        return toFieldName;
    }

    public String getFromCardinal() {
        return "1";
    }

    public String getToCardinal() {
        return toCardinal;
    }

    @Override
    public String toString() {
        String fname = null == getMessage() ? "" : " : " + getMessage();
        return String.format("%s \"%s\" %s \"%s\" %s %s",
                from, getFromCardinal(), RELATION_TYPE_AGGREGATION, toCardinal, to, fname
            );
    }
}
