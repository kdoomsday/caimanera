name := """caimanera"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  cache,
  "com.mohiva" %% "play-silhouette" % "3.0.0-RC2",
  "com.kyleu" %% "jdub-async" % "1.0",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.github.nscala-time" %% "nscala-time" % "2.10.0",
  "joda-time" % "joda-time" % "2.9.2"
)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "notifyjs" % "0.3.2",
  "org.webjars" % "bootstrap-datetimepicker" % "2.3.5"
)

routesGenerator := InjectedRoutesGenerator

fork in run := false