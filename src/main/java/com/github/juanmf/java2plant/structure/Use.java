package com.github.juanmf.java2plant.structure;

import java.lang.reflect.Member;

/**
 * @author juanmf@gmail.com
 */
public class Use implements Relation {
    public static final String RELATION_TYPE_USE = " .down.> ";
    private final String msg;
    private final Class<?> from;
    private final String to;
    private final Member originatingMember;

    public Use(Class<?> from, String to, Member originatingMember) {
        this(from, to, originatingMember, null);
    }

    public Use(Class<?> from, String to, Member originatingMember, String msg) {
        this.from = from;
        this.to = to;
        this.msg = msg;
        this.originatingMember = originatingMember;
    }

    public Class<?> getFromType() {
        return from;
    }

    @Override
    public Member getOriginatingMember() {
        return null;
    }

    public String getToType() {
        return to;
    }

    public String getRelationType() {
        return RELATION_TYPE_USE;
    }

    public String getMessage() {
        return msg;
    }

    public String getFromCardinal() {
        return null;
    }

    public String getToCardinal() {
        return null;
    }

    @Override
    public String toString() {
        String fname = null == getMessage() ? "" : " : " + getMessage();
        return String.format("%s %s %s %s", from, RELATION_TYPE_USE, to, fname);
    }
}
