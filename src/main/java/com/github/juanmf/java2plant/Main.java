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
