name := "Funnel"

version := "0.0.1"

scalaVersion := "2.10.2"

resolvers += "Baidu Maven Repository" at "http://maven.scm.baidu.com:8081/nexus/content/groups/public"

libraryDependencies += "junit" % "junit" % "4.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10-M2" % "test"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.6"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.7"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.21"

