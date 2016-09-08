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
package com.github.juanmf.java2plant.render;

/**
 * @author juanmf@gmail.com
 */
public class NotesRenderer {
    static final java.lang.String NOTE_RELEVANT_CLASS = "Relevant Class";
    private int notesCounter = 0;

    NotesRenderer() {
    }

    /**
     * Renders a named note line, and links it to type in Diagram src.
     *
     * @param sb   The Diagram src to add the note to.
     * @param note The note text
     * @param type the type to link to. Might be null if we want the note unlinked.
     */
    public void render(StringBuilder sb, String note, String type) {
        sb.append("Note \"").append(note).append("\" as N").append(++notesCounter).append("\n");
        if (null != type) {
            sb.append(type).append(" .. ").append("N").append(notesCounter).append("\n");
        }
    }

    public void render(StringBuilder sb, String note) {
        render(sb, note, null);
    }
}
