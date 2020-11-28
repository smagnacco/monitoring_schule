lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion     = "2.6.10"
lazy val kamonVersion    = "2.1.9"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "scala.schule",
      scalaVersion    := "2.13.3"
    )),
    name := "monitor",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "io.kamon" %% "kamon-bundle" % kamonVersion,
      "io.kamon" %% "kamon-prometheus" % kamonVersion,
      "io.kamon" %% "kamon-zipkin" % kamonVersion,
      "io.kamon" %% "kamon-annotation" % kamonVersion,
      "io.kamon" %% "kamon-status-page" % kamonVersion,
      "io.kamon" % "kanela-agent" % "1.0.7" % "agent",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )
