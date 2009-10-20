Running:
  $ mvn install
  $ cd gwt-client
  $ mvn jetty:run

Eclipse setup:
  $ mvn eclipse:eclipse

Rapid development setup:
  1. Using maven-jetty-plugin:
     $ mvn jetty:run
     This will do a "gwt-compile -> run-webapp" step with "overlays" from 
     the ${rootArtifactId}-server webapp
     
  2. Using google-eclipse-plugin:
     a. Enable the checkbox "Use Google Web Toolkit" by right-clicking:
     ${rootArtifactId}-gwt-client and selecting Properties->Google->Web Toolkit

     b. Edit the .project file and append the ff on the <natures> element:
        <nature>com.google.gdt.eclipse.core.webAppNature</nature>
     
     Note that on GWT hosted mode complains about ClassCastException on a gwt 
     overlay (containing enum-like strings) when you test against a server backend.
     
     If you want to test the UI on hosted-mode, execute:
     $ mvn -Dgwt.compiler.skip=true jetty:run
     
     When jetty is started, open run configurations and uncheck 
     "Run built-in server" and click "Run".
     
     That is done by right-clicking:
     ${rootArtifactId}-gwt-client and selecting Run As->Run Configurations->Run

