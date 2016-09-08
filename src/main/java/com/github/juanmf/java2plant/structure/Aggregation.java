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
    private boolean printedAsMember;

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
                from.getName(), getFromCardinal(), RELATION_TYPE_AGGREGATION, toCardinal, to, fname
            );
    }

    @Override
    public void setPrintedAsMember(boolean printedAsMember) {
        this.printedAsMember = printedAsMember;
    }

    @Override
    public boolean getPrintedAsMember() {
        return printedAsMember;
    }
}
