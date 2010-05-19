Running:
  $ mvn install
  $ cd gwt-client
  $ mvn -Dgwt.compiler.skip=true -Dprotostuff.compiler.skip=true jetty:run

Eclipse setup:
  $ mvn eclipse:eclipse

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
    
     To explicitly compile only the gwt overlays:
     $ mvn protostuff:compile
     
     When developing on hosted mode, enable the protostuff compiler option "dev_mode".
     If not, the GWT compiler will throw ClassCastException on an enum emulated by an integer (casting).
     This affects only gwt overlays that have enums as declared in the .proto file.
     
     If you want to test the compiled UI, execute:
     $ mvn -Dgwt.compiler.skip=true jetty:run
     
     When jetty is started, open run configurations and uncheck 
     "Run built-in server" and click "Run".
     
     That is done by right-clicking:
     ${rootArtifactId}-gwt-client and selecting Run As->Run Configurations->Run

