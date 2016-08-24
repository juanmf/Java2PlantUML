package com.github.juanmf.java2plant.render;

import com.github.juanmf.java2plant.render.event.RejectingBaseInterfaceEvent;
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
            sb.append(imp.asLollipop()).append("\n");
        } catch (ClassCastException ex) {
            // do nothing
        }
    }
}
