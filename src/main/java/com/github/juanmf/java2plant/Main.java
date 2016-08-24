package com.github.juanmf.java2plant;


import com.github.juanmf.java2plant.render.filters.Filters;

/**
 * @author juanmf@gmail.com
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        String thePackage = 1 == args.length ? args[0] : "com.github.juanmf.java2plant";
        System.out.println(Parser.parse(
                thePackage, Filters.FILTER_CHAIN_RELATION_TYPE_STANDARD, Filters.FILTER_CHAIN_CLASSES_STANDARD,
                Filters.FILTER_CHAIN_RELATION_STANDARD));
    }
}
