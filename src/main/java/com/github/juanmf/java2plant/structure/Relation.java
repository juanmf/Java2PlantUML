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
public interface Relation {
    Class<?> getFromType();
    Member getOriginatingMember();
    String getToType();

    String getRelationType();
    String getMessage();
    String getFromCardinal();
    String getToCardinal();

    /**
     * Must print a plant UML relation line, like:
     * <code>
     *     AcallerClass --> SomeOtherClass : message()
     *     AsubClass -|> ParentClass
     *     AusingClass ..> usedClass
     * </code>
     *
     * @return
     */
    String toString();
    void setPrintedAsMember(boolean printedAsMember);
    boolean getPrintedAsMember();

}
