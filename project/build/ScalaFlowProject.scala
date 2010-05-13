import sbt._

class ScalaFlowProject(info: ProjectInfo) extends ParentProject(info) with IdeaPlugin
{
   val mavenLocal = "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

   // val junit = "junit" % "junit" % "4.4" % "test"
   // val specs = "org.scala-tools.testing" % "specs" % "1.6.0" % "test"

   lazy val core = project("scalaflow-core", "ScalaFlow Core", new ScalaFlowCoreProject(_))
   lazy val ui = project("scalaflow-grapher", "ScalaFlow workflow visualisation", new ScalaFlowGrapherProject(_), core)
   
   
   class ScalaFlowGrapherProject(info: ProjectInfo) extends DefaultProject(info) with IdeaPlugin
   {
	val guiceGrapher = "com.google.inject.extensions" % "guice-grapher" % "2.0"
   }
   
   class ScalaFlowCoreProject(info: ProjectInfo) extends DefaultProject(info) with IdeaPlugin
   {
	   val junit = "junit" % "junit" % "4.4" % "test"
	    val specs = "org.scala-tools.testing" % "specs" % "1.6.0" % "test"
//	   val specs = "org.scala-tools.testing" % "specs" % "1.6.6-2.8.0.Beta1-RC6" % "test"
   }

}