Running:
$ cd target
$ java -jar protostuff-codegen-1.0-SNAPSHOT-jar-with-dependencies.jar modules.properties

==== modules.properties ====

modules = foo

foo.fullClassname = com.example.bar.OuterClassname
foo.outputPackage = com.example.bar.generated
foo.generator = json
foo.encoding = UTF-8
foo.outputDir = src/main/java

===========================


