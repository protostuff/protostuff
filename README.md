
![Protostuff](https://protostuff.github.io/images/protostuff_300x100.png)

A serialization library with built-in support for forward-backward compatibility (schema evolution) and validation.

Documentation:

1. https://protostuff.github.io/

Benchmarks:

1. http://hperadin.github.io/jvm-serializers-report/report.html

Usage (maven)
-------------

1. When you generate schemas for your classes
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.5.2</version>
  </dependency>
  ```

2. Runtime schemas
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.5.2</version>
  </dependency>
  ```

Questions/Concerns/Suggestions
------------------------------

- subscribe to http://groups.google.com/group/protostuff/

Requirements
------------

Java 1.6 or higher

Build Requirements
------------------

Maven 3.2.3 or higher

Developing with eclipse
------------------
```sh
mvn install && mvn eclipse:eclipse
# Open eclipse, import existing project, navigate to the protostuff module you're after, then hit 'Finish'.
```
