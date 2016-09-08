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
package com.github.juanmf.java2plant.render.filters;

import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.render.event.RenderEvent;

/**
 * @param <T> Must be the same type used as parameter to the Filter for which this notifier is being set.
 *
 * @author juanmf@gmail.com
 */
public class NotifierOnFiltering <T> {

    private final boolean notify;
    private final OnResult onResult;
    private RenderEvent<T> event;

    public NotifierOnFiltering(OnResult onResult, RenderEvent<T> event) {
        this.onResult = onResult;
        this.event = event;
        this.notify = true;
    }

    public NotifierOnFiltering() {
        onResult = null;
        this.notify = false;
    }

    boolean getResultAndNotify(boolean filterResult, T filteringObject, StringBuilder sb) {
        if (! notify) {
            return filterResult;
        }
        event.setScriptStringBuilder(sb);
        try {
            event.setFilteringObject(filteringObject);
            onResult.fire(filterResult, event);
        } catch (ClassCastException ex) {
            // do nothing
        }
        return filterResult;
    }

    public enum OnResult {
        SUCCESS(true), FAILURE(false);
        boolean result;

        OnResult(boolean result) {
            this.result = result;
        }

        void fire(boolean filterResult, RenderEvent event) {
            if (this.result == filterResult) {
                Parser.getEventBus().post(event);
            }
        }
    }
}
