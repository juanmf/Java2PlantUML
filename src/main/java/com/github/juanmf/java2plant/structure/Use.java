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
public class Use implements Relation {
    public static final String RELATION_TYPE_USE = " .down.> ";
    private final String msg;
    private final Class<?> from;
    private final String to;
    private final Member originatingMember;
    private boolean printedAsMember;

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
        return originatingMember;
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
        return String.format("%s %s %s %s", from.getName(), RELATION_TYPE_USE, to, fname);
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
