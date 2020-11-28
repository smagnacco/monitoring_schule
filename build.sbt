import com.despegar.sbt.madonna.Madonna.MadonnaKeys
import sbt.{enablePlugins, Resolver}
import sbtbuildinfo.BuildInfoPlugin
import sbtrelease.ReleasePlugin.autoImport.{ReleaseStep, _}
import sbtrelease.ReleaseStateTransformations._

import scala.concurrent.duration._

val artifactId = "mariekondo"

organization := "com.despegar"
name := artifactId
scalaVersion := "2.13.4"

fork in run := sys.props.get("fork").isDefined

semanticdbEnabled := true // enable SemanticDB
semanticdbVersion := semanticVersion
addCompilerPlugin(scalafixSemanticdb)

scalafmtOnCompile := true

scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-deprecation",
  "-Ywarn-unused",
  "-language:dynamics", //required by dijon
  "-Yrangepos", // required by SemanticDB compiler plugin
  "-Ywarn-unused:imports", // required by `RemoveUnused` rule
  "-Wunused:imports"
)

val akkaVersion = "2.6.10"
val akkaHttpVersion = "10.2.1"
val akkaHttpJsonVersion = "1.35.2"
val akkaPersistenceCassandra = "0.105"
val swaggerVersion = "2.1.5"
val pureConfigVersion = "0.14.0"
val jsoniterVersion = "2.6.2"
val kamonVersion = "2.1.9"
val kanelaVersion = "1.0.7"
val semanticVersion = "4.4.0"
val scalatestVersion = "3.2.3"
val catsVersion = "2.2.0"
val newrelicVersion = "6.2.1"
val scyllaVersion = "3.10.1-scylla-0"
val phantomVersion = "2.59.0"
val lz4Version = "1.7.1"
val elastic4sVersion = "7.9.2"
val scalafmtVersion = "2.7.5"
val zstdVersion = "1.4.5-12"

resolvers ++= Seq(
  ("Nexus tilcara" at "http://vmtilcara.despexds.net:8080/nexus/content/repositories/snapshots")
    .withAllowInsecureProtocol(true),
  ("Nexus despegar miami" at "http://nexus.despexds.net:8080/nexus/content/groups/public/")
    .withAllowInsecureProtocol(true),
  Resolver.jcenterRepo,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("releases").withAllowInsecureProtocol(true),
  Resolver.bintrayRepo("kamon-io", "releases"),
  Resolver.bintrayRepo("kamon-io", "snapshots"),
  Resolver.bintrayRepo("outworkers", "oss-releases")
)

libraryDependencies ++= {
  Seq(
    "com.despegar.gaia" %% "gondola-libs-predictions" % "0.0.7",
    "org.slf4j" % "slf4j-api" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
    "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-cassandra" % akkaPersistenceCassandra excludeAll (ExclusionRule(
      "com.typesafe.akka",
      "akka-persistence-query"
    ), ExclusionRule("com.datastax.cassandra", "cassandra-driver-core")),
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
    "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.5-akka-2.6.x",
    "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1",
    "org.json4s" %% "json4s-jackson" % "3.6.10",
    "de.heikoseeberger" %% "akka-http-json4s" % akkaHttpJsonVersion,
    "de.heikoseeberger" %% "akka-http-jsoniter-scala" % akkaHttpJsonVersion,
    "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % jsoniterVersion % Compile,
    "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % jsoniterVersion % Provided, // required only in compile-time
    //"com.github.pathikrit" %% "dijon" % dijonVersion,
    "com.despegar.library" % "routing" % "2.2.2",
    "com.despegar.cloudia" % "vault-sensitive-config" % "0.0.1",
    "io.kamon" %% "kamon-bundle" % kamonVersion,
    "io.kamon" %% "kamon-prometheus" % kamonVersion,
    "io.kamon" %% "kamon-zipkin" % kamonVersion,
    "io.kamon" %% "kamon-annotation" % kamonVersion,
    "io.kamon" %% "kamon-status-page" % kamonVersion,
    "io.kamon" % "kanela-agent" % kanelaVersion % "agent",
    "com.newrelic.agent.java" % "newrelic-agent" % newrelicVersion % "agent",
    "com.newrelic.agent.java" % "newrelic-api" % newrelicVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "org.scalatest" %% "scalatest-funsuite" % scalatestVersion % "test",
    "org.scalatest" %% "scalatest-mustmatchers" % scalatestVersion % "test",
    "org.scalactic" %% "scalactic" % scalatestVersion % "test",
    "com.github.pureconfig" %% "pureconfig-enumeratum" % pureConfigVersion,
    "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
    "com.github.pureconfig" %% "pureconfig-core" % pureConfigVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "org.scalameta" %% "scalafmt-dynamic" % scalafmtVersion % "compile",
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.2.0",
    "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.1.3",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.3",
    "io.swagger.core.v3" % "swagger-core" % swaggerVersion,
    "io.swagger.core.v3" % "swagger-annotations" % swaggerVersion,
    "io.swagger.core.v3" % "swagger-models" % swaggerVersion,
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "com.scylladb" % "scylla-driver-core" % scyllaVersion,
    "com.github.luben" % "zstd-jni" % zstdVersion,
    "org.lz4" % "lz4-java" % lz4Version,
    "com.outworkers" %% "phantom-dsl" % phantomVersion excludeAll ExclusionRule("com.datastax.cassandra",
                                                                                "cassandra-driver-core"),
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion excludeAll ExclusionRule(
      "org.apache.logging.log4j"
    ),
    "com.sksamuel.elastic4s" %% "elastic4s-json-json4s" % elastic4sVersion excludeAll ExclusionRule(
      "org.apache.logging.log4j"
    ),
    "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion excludeAll ExclusionRule(
      "org.apache.logging.log4j"
    )
  )
}

//ADD DIJON MASTER BRANCH, AS A DEPENDENCY FOR THE PROJECT
//lazy val dijon = ProjectRef(uri("https://github.com/pathikrit/dijon.git#master"), "dijon")
//lazy val root = (project in file(".")).dependsOn(dijon)

mainClass := Some("com.despegar.mariekondo.AppServer")

enablePlugins(BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion)

buildInfoPackage := "com.despegar.mariekondo"

ivyConfigurations += Madonna.AgentConfiguration

unmanagedSourceDirectories in Compile ++=
  Seq(
    baseDirectory.value / "src/main/resources"
  )

//Notar que hay que comentar la linea siguiente en DEV mode (sino no usa el logback-test y quiere loguear en prod
mappings in (Compile, packageBin) ~= {
  _.filter(t => t._1.getName != "logback-test.xml")
}

MadonnaKeys.filesFromProject ++= List(s"${baseDirectory.value.getPath}/src/main/resources/mariekondo")

MadonnaKeys.healthCheckURI := "/mariekondo/health-check"

MadonnaKeys.healthCheckTimeout := 400.seconds

publishTo := {
  if (version.value.endsWith("SNAPSHOT"))
    Some(
      ("Nexus snapshots" at "http://nexus.despexds.net:8080/nexus/content/repositories/snapshots/")
        .withAllowInsecureProtocol(true)
    )
  else
    Some(
      ("Nexus releases" at "http://nexus.despexds.net:8080/nexus/content/repositories/releases/")
        .withAllowInsecureProtocol(true)
    )
}

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
