Requirements:
Unix:
  Make sure you have locally built the protocol buffer compiler.
  Its available at http://protobuf.googlecode.com/files/protobuf-2.3.0.tar.gz
  This is so you can execute "protoc" on the command line from anywhere.

Windows:
  Download http://protobuf.googlecode.com/files/protoc-2.3.0-win32.zip
  If you extracted it on c:\foo, add c:\foo to the PATH variable.
  This is so you can execute "protoc" on the command line from anywhere.

Running:
  $ mvn install
  $ cd gwt-client
  $ mvn -Dgwt.compiler.skip=true jetty:run

Rapid development setup:
  1. Using maven-jetty-plugin:
     $ mvn jetty:run
     This will do a "gwt-compile -> run-webapp" step with "overlays" from 
     the ${rootArtifactId}-server webapp

  2. Using google-eclipse-plugin:
     Edit the .project file:
     1. Append the ff on the <buildSpec> element:
        <buildCommand>
          <name>com.google.gdt.eclipse.core.webAppProjectValidator</name>
        </buildCommand>
        <buildCommand>
          <name>com.google.gwt.eclipse.core.gwtProjectValidator</name>
        </buildCommand>
     
     2. Append the ff on the <natures> element:
        <nature>com.google.gdt.eclipse.core.webAppNature</nature>
        <nature>com.google.gwt.eclipse.core.gwtNature</nature>
        
     Edit the .classpath file:
     1. Append the ff on the <classpath> element:
        <classpathentry kind="con" path="com.google.gwt.eclipse.core.GWT_CONTAINER"/>
        
     2. Change "<classpathentry kind="output" path="target/classes"/>" to:
        <classpathentry kind="output" path="war/WEB-INF/classes"/>
        
     3. Copy server/src/main/webapp/WEB-INF/web.xml to gwt-client/war/WEB-INF/
     
     Note that the GWT compiler will throw ClassCastException on an enum emulated by an integer (casting).
     This affects only gwt overlays that have enums as declared in the .proto file.
     
     If you want to test the compiled UI, execute:
     $ mvn -Dgwt.compiler.skip=true jetty:run
