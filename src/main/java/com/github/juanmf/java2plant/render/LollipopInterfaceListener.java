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

import com.github.juanmf.java2plant.render.event.RejectingBaseInterfaceEvent;
import com.github.juanmf.java2plant.render.filters.Filters;
import com.github.juanmf.java2plant.structure.Implementation;
import com.google.common.eventbus.Subscribe;

/**
 * @author juanmf@gmail.com
 */
public class LollipopInterfaceListener {
    @Subscribe
    public void handle(RejectingBaseInterfaceEvent e) {
        StringBuilder sb = e.getScriptStringBuilder();
        try {
            Implementation imp = (Implementation) e.getFilteringObject();
            if (! Filters.FILTER_RELATION_FORBID_FROM_BASE.satisfy(imp, sb)) {
                return;
            }
            sb.append(imp.asLollipop()).append("\n");
        } catch (ClassCastException ex) {
            // do nothing
        }
    }
}
