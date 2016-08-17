# Java2PlantUML
This maven plugin allows you to inspect compile time relations on classes 
within the class path of your projects, and its dependencies. And renders a
Class Diagram src for PlantUML in cli output as 

```
[INFO] Following is the PlantUML src: 
@startuml
' Created by juanmf@gmail.com

' Participants 

class com.something.AClass

' Relations 

com.something.AClass  .down.>  info.magnolia.module.googlesitemap.service.SiteMapXMLUtil  : StaticPublisher()
com.something.AClass  .down.>  javax.jcr.Node  : setRootNode()
com.something.AClass  .down.>  boolean  : setPreview()
com.something.AClass  .down.>  java.lang.String  : setWorkspace()
com.something.AClass  .down.>  org.apache.http.client.HttpClient  : StaticPublisher()
com.something.AClass  .down.>  info.magnolia.cms.i18n.I18nContentSupport  : StaticPublisher()
com.something.AClass  .down.>  info.magnolia.dam.api.AssetProviderRegistry  : StaticPublisher()
com.something.AClass  .down.>  class java.lang.String  : setPublishingLanguages()java.util.Collection
@enduml

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.295 s
[INFO] Finished at: 2016-08-02T08:19:29-03:00
[INFO] Final Memory: 17M/212M
[INFO] ------------------------------------------------------------------------

mvn clean install
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

Then add the plugin markup to your pom.xml.
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

Then step at your project's pom directory an run

```
mvn   -Dparse.thePackage="com.something.AClass" clean compile java2PlantUML:parse 
// or
mvn   -Dparse.thePackage="com.something.apackage" clean compile java2PlantUML:parse 

```
parse.thePackage is the root from where class scanning will get the main 
classes of your Diagram.

You might want to remove the plugin markup from your pom after you got the desired Diagrams.


TODO
====
 * Add Filters to CLI params
 instead of arrows.
 * represent Type parameters in class definitions i.e. public class SelectFieldFactory<D extends SelectFieldDefinition>
 * Render several diagrams, making focus on classes inside given packages.
