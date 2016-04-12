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

  // scalikejdbc
  "org.scalikejdbc"               %% "scalikejdbc"                         % "2.3.5",
  "org.scalikejdbc"               %% "scalikejdbc-config"                  % "2.3.5",
  "org.scalikejdbc"               %% "scalikejdbc-play-initializer"        % "2.5.0",

  // Others
  "mysql"                          % "mysql-connector-java"                % "5.1.36",
  "com.h2database"                 % "h2"                                  % "1.4.191",
  "org.mindrot"                    % "jbcrypt"                             % "0.3m",
  "org.scalaz"                    %% "scalaz-core"                         % "7.1.7",

  // Test
  "org.scalatest"                 %% "scalatest"                           % "2.2.6"                 % "test",
  "org.scalatestplus"             %% "play"                                % "1.4.0-M3"              % "test",
  "jp.t2v"                        %% "play2-auth-test"                     % "0.14.2"                % "test"
)


// scalariform
import scalariform.formatter.preferences._

scalariformSettings


// Slick/Playのgenerate したscalaファイルを除外する
excludeFilter in scalariformFormat := new SimpleFileFilter(target => {
  val baseDirectory = file(".").getCanonicalPath
  lazy val relativePath = target.getCanonicalPath.stripPrefix(baseDirectory)
  target.getName == "Tables.scala" ||
    relativePath.startsWith("/target/scala-2.11/routes")
})

scalariformPreferences := scalariformPreferences.value
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)


// prompt
import com.scalapenos.sbt.prompt._
import SbtPrompt.autoImport._

val customPromptTheme = PromptTheme(
  List(
    text("[", fg(white)),
    currentProject(fg(cyan)),
    text("] ", fg(white)),
    gitBranch(clean = fg(green), dirty = fg(red)),
    text(" $ ", fg(yellow))
  )
)


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
  promptTheme := customPromptTheme,
  shellPrompt := (implicit state => promptTheme.value.render(state))
).settings(
  maintainer in Docker := "Tomofumi Tanaka <tanacasino@gmail.com>",
  dockerExposedPorts in Docker := Seq(9000)
)

