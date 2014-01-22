name := "ExtremeSpotsServer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.google.code.gson" % "gson"  % "2.2.4",
  "org.apache.jena" % "apache-jena-libs" % "2.11.0",
  "commons-io" % "commons-io" % "2.4",
  "org.apache.jena" % "jena-fuseki" % "0.2.7"
)     

play.Project.playJavaSettings
