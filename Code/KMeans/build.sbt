name := "KMeans"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "18.0",
  "com.typesafe.akka" %% "akka-actor"   % "2.3.7",
  "com.typesafe.akka" %% "akka-slf4j"   % "2.3.7",
  "com.typesafe.akka" %% "akka-remote"  % "2.3.7",
  "com.typesafe.akka" %% "akka-agent"   % "2.3.7",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test",
  "commons-lang" % "commons-lang" % "2.6"
)

version := "1.0"