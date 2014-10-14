name := "KMeans"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "18.0",
  "com.typesafe.akka" %% "akka-actor"   % "2.3.6",
  "com.typesafe.akka" %% "akka-slf4j"   % "2.3.6",
  "com.typesafe.akka" %% "akka-remote"  % "2.3.6",
  "com.typesafe.akka" %% "akka-agent"   % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test"
)

version := "1.0"