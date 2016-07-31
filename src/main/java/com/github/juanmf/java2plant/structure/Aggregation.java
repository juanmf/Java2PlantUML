package com.github.juanmf.java2plant.structure;

/**
 * @author juanmf@gmail.com
 */
public class Aggregation implements Relation {
    public static final String RELATION_TYPE_AGGREGATION = " o-left- ";
    private final String toFieldName;
    private final String from;
    private final String to;
    private final String toCardinal;

    public Aggregation(String from, String to, String toCardinal) {
        this(from, to, toCardinal, null);
    }

    public Aggregation(String from, String to, String toCardinal, String toFieldName) {
        this.from = from;
        this.to = to;
        this.toCardinal = toCardinal;
        this.toFieldName = toFieldName;
    }

    public String getFromType() {
        return from;
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
