# Java2PlantUML
This maven plugin allows you to inspect compile time relations on classes 
within the class path of your projects, and its dependencies. And renders a
Class Diagram src for [PlantUML](http://plantuml.com/) in cli output as 

```
[INFO] Following is the PlantUML src: 
@startuml
' Created by juanmf@gmail.com

' Using left to right direction to try a better layout feel free to edit
left to right direction
' Participants 

class com.github.juanmf.java2plant.render.filters.PredicateFilter {
#  predicate : Predicate
--
+  satisfy()  : boolean

}
class...

' Relations

com.github.juanmf.java2plant.render.filters.Filters "1"  o-left-  "1" com.github.juanmf.java2plant.render.filters.RelationFieldsFilter  : FILTER_RELATION_FORBID_TO_BASE
com...
@enduml

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.295 s
[INFO] Finished at: 2016-08-02T08:19:29-03:00
[INFO] Final Memory: 17M/212M
[INFO] ------------------------------------------------------------------------

```

Installation
============

To use it as a plugin, 1st you need install it in local repo as follows. Step 
at this project's pom Directory and run

```
mvn clean install
```

Usage
=====

Then add the plugin markup to your desired project's pom.xml.
For the sake of example, this plugin markup is included in its own pom.xml so 
you can run the plugin in this project too (after installing, of course).

```xml
<project>
  ...
  <build>
    ...
    <plugins>
      ...
      <plugin>
         <groupId>com.github.juanmf</groupId>
         <artifactId>java2PlantUML-maven-plugin</artifactId>
         <version>1.0.1</version>
         <configuration>
           <goalPrefix>java2PlantUML</goalPrefix>
         </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

Then step at your project's pom directory and run

```
mvn   -Dparse.thePackage="com.something.AClass" clean compile java2PlantUML:parse 
// or
mvn   -Dparse.thePackage="com.something.apackage" clean compile java2PlantUML:parse 

```
`parse.thePackage` is the root from where class scanning will get the main 
classes of your Diagram, it can be a package or a FQCN.

You might want to remove the plugin markup from your pom after you got the desired Diagrams.

Results
=======

The end result is a file named "j2puml+now+.txt" that you can process with [PlantUML online Render](http://plantuml.com/plantuml) in order to get the UML diagram rendered 
by PlantUML as per the instructions in the generated script. A run over this project renders:

![java2Plant diagram should appear here..](/doc/java2Plant.png?raw=true "Java2Plant Collaboration")


TODO
====
 * Add Filters to CLI params
 * represent Type parameters in class definitions i.e. public class SelectFieldFactory<D extends SelectFieldDefinition>
 * Render several diagrams, making focus on classes inside given packages.
 
