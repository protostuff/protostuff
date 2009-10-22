Requirements:
Unix:
  Make sure you have locally built the protocol buffer compiler.
  Its available at http://protobuf.googlecode.com/files/protobuf-2.2.0.tar.gz
  This is so you can execute "protoc" on the command line from anywhere.

Windows:
  Download http://protobuf.googlecode.com/files/protoc-2.2.0-win32.zip
  If you extracted it on c:\foo, add c:\foo to the PATH variable.
  This is so you can execute "protoc" on the command line from anywhere.

Running:
  $ mvn install
  $ cd server
  $ mvn jetty:run

Eclipse setup:
  $ mvn eclipse:eclipse

Rapid development setup:
  Use maven-jetty-plugin.
  $ mvn jetty:run

