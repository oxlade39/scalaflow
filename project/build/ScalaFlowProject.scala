import sbt._

class ScalaFlowProject(info: ProjectInfo) extends ParentProject(info) //with IdeaPlugin
{
   val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

   lazy val core = project("scalaflow-core", "ScalaFlow Core", new ScalaFlowCoreProject(_))
   lazy val ui = project("scalaflow-grapher", "ScalaFlow workflow visualisation", new ScalaFlowGrapherProject(_), core)
   
   
   class ScalaFlowGrapherProject(info: ProjectInfo) extends DefaultProject(info) //with IdeaPlugin
   {
		val guiceGrapher = "com.google.inject.extensions" % "guice-grapher" % "2.0"
   }
   
   class ScalaFlowCoreProject(info: ProjectInfo) extends DefaultProject(info) //with IdeaPlugin
   {
		val junit = "junit" % "junit" % "4.7" % "test"
	    val specs = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5" % "test"
		val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test"
   }

}