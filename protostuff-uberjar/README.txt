Building an "uberjar" for using protostuff with OSGI.

1) build the normal protostuff using maven
- in the main project directory:
   mvn clean install

2) switch to protostuff-uberjar
   cd protostuff-uberjar

3) run maven to create the OSGI-bundle
   mvn clean bundle:manifest assembly:single install


Background:
- OSGI requires special entries in the manifest file.
  (see src/main/resources/MANIFEST.MF)
- As we will need multiple .jar files from the project,
  there may be (at least I had) issues with exporting the same
  packagename from multiple jar files.
  (most of the protostuff-* jars have packages in com.dyuproject.protostuff)
- to avoid this, all class files should reside in a single .jar
  or OSGI bundle which exports com.dyuproject.protostuff
- to achieve this, we are using the maven assembly plugin.
- the assembly plugin simply packs all the projects dependencies
  into a single .jar file.
- But the MANIFEST file should be mostly "auto-generated" and needs
  OSGI-specific modifications.
- for this we use the maven-bundle-plugin.

To include further packages, please make sure the dependencies are met
in the Import statement. Example: the Json jar depends on the codehaus jackson
json package/bundle.


  

