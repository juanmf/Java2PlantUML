package com.github.juanmf.java2plant.structure;

import java.util.Comparator;

/**
 * @author juanmf@gmail.com
 */
public class Inner {
    Relation rel;

    public class InnerInner {

    }

    Comparator<String> anonimous = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return 0;
        }
    };
}
