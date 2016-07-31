package com.github.juanmf.java2plant.structure;

/**
 * @author juanmf@gmail.com
 */
public class Use implements Relation {
    public static final String RELATION_TYPE_USE = " .down.> ";
    private final String msg;
    private String from;
    private String to;

    public Use(String from, String to) {
        this(from, to, null);
    }

    public Use(String from, String to, String msg) {
        this.from = from;
        this.to = to;
        this.msg = msg;
    }

    public String getFromType() {
        return from;
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
