package com.github.juanmf.java2plant.structure;

/**
 * @author juanmf@gmail.com
 */
public class Extension implements Relation {
    public static final String RELATION_TYPE_EXTENSION = " -up|> ";
    private String from;
    private String to;

    public Extension(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFromType() {
        return from;
    }

    public String getToType() {
        return to;
    }

    public String getRelationType() {
        return RELATION_TYPE_EXTENSION;
    }

    public String getMessage() {
        return null;
    }

    public String getFromCardinal() {
        return null;
    }

    public String getToCardinal() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", from, RELATION_TYPE_EXTENSION, to);
    }
}
