// Turn this project into a Scala.js project by importing these settings

import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Snake Scala.js"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0"
)

bootSnippet := "snake.SnakeApp().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
