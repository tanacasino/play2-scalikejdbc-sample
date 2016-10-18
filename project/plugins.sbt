// Playframework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")

// Scalariform
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

// ScalijeJDBC Generator
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.40"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.4.2")

