// play2-scalikejdbc-sample

val appName = "play2-scalikejdbc-sample"

val appVersion = "1.0.0"

val baseSettings = Seq(
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xlint",
    "-Ywarn-dead-code",
    //"-Ywarn-unused-import",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions"
  ),
  javacOptions in compile ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.8",
    "-target", "1.8"
  )
)

val appDependencies = Seq(
  // Play libs
  cache,
  ws,

  // Play Thirdparty libs
  "jp.t2v"                        %% "play2-auth"                          % "0.14.2",
  "com.github.mumoshu"            %% "play2-memcached-play24"              % "0.7.0",
  "org.flywaydb"                  %% "flyway-play"                         % "3.0.1",

  // scalikejdbc
  "org.scalikejdbc"               %% "scalikejdbc"                         % "2.4.2",
  "org.scalikejdbc"               %% "scalikejdbc-config"                  % "2.4.2",
  "org.scalikejdbc"               %% "scalikejdbc-jsr310"                  % "2.4.2",
  "org.scalikejdbc"               %% "scalikejdbc-play-initializer"        % "2.5.1",

  // Others
  "mysql"                          % "mysql-connector-java"                % "5.1.40",
  "com.h2database"                 % "h2"                                  % "1.4.192",
  "org.mindrot"                    % "jbcrypt"                             % "0.3m",
  "org.scalaz"                    %% "scalaz-core"                         % "7.1.7",

  // Test
  "org.scalatestplus.play"        %% "scalatestplus-play"                  % "1.5.1"                 % "test",
  "jp.t2v"                        %% "play2-auth-test"                     % "0.14.2"                % "test"
)


// scalariform
import scalariform.formatter.preferences._

scalariformSettings

// Playの generate したscalaファイルを除外する
excludeFilter in scalariformFormat := new SimpleFileFilter(target => {
  val baseDirectory = file(".").getCanonicalPath
  lazy val relativePath = target.getCanonicalPath.stripPrefix(baseDirectory)
  relativePath.startsWith("/target/scala-2.11/routes")
})

scalariformPreferences := scalariformPreferences.value
  .setPreference(DoubleIndentClassDeclaration, false)

scalikejdbcSettings

import play.sbt.PlayImport.PlayKeys._

// Main
lazy val root = Project(
  appName,
  file(".")
).enablePlugins(PlayScala).settings(
  baseSettings: _*
).settings(
  version := appVersion,
  playDefaultPort := 9000
).settings(
  libraryDependencies ++= appDependencies
).settings(
  maintainer in Docker := "Tomofumi Tanaka <tanacasino@gmail.com>",
  dockerExposedPorts in Docker := Seq(9000)
)

