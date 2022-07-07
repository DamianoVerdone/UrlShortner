lazy val commonSettings = Seq(
  name := "urlShortner",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.13.8",
  organization := "com.enelx",
  scalacOptions ++= Seq(
    "-deprecation",
    "-Xfatal-warnings",
    "-Ywarn-value-discard",
    "-Xlint:missing-interpolator"
  ),
)

lazy val Http4sVersion = "0.23.10"

lazy val CirceVersion = "0.14.1"

lazy val LogbackVersion = "1.2.3"

lazy val ScalaTestVersion = "3.2.3"
lazy val ScalaMockVersion = "5.1.0"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server"  % Http4sVersion,
      "org.http4s"            %% "http4s-circe"         % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"           % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client"  % Http4sVersion     % "it,test",

      "io.circe"              %% "circe-generic"        % CirceVersion,
      "io.circe"              %% "circe-literal"        % CirceVersion      % "it,test",
    //  "io.circe"              %% "circe-optics"         % CirceVersion      % "it",


      "ch.qos.logback"        %  "logback-classic"      % LogbackVersion,

      "org.scalatest" %% "scalatest" % "3.2.9"  % "it,test",
      "org.scalamock"         %% "scalamock"            % ScalaMockVersion  % "test"
    )
  )
