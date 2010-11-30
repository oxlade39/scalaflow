import sbt._

class ScalaFlowProject(info: ProjectInfo) extends ParentProject(info) with Exec //with IdeaPlugin
{	
   val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
   val scalaSnapshots = "Scala Snapshots repo" at "http://scala-tools.org/repo-snapshots"

   lazy val core = project("scalaflow-core", "ScalaFlow Core", new ScalaFlowCoreProject(_))
   lazy val ui = project("scalaflow-grapher", "ScalaFlow workflow visualisation", new ScalaFlowGrapherProject(_), core)
   lazy val atompub = project("scalaflow-atompub", "ScalaFlow AtomPub", new ScalaFlowAtomPub(_), core)
   
   
   class ScalaFlowGrapherProject(info: ProjectInfo) extends DefaultProject(info) //with IdeaPlugin
   {	
		val guiceGrapher = "com.google.inject.extensions" % "guice-grapher" % "2.0"
   }
   
   class ScalaFlowCoreProject(info: ProjectInfo) extends DefaultProject(info) //with IdeaPlugin
   {
   		override def includeTest(s: String) = { s.endsWith("Spec") || s.endsWith("Specification") || s.endsWith("Unit")  }		

		val junit = "junit" % "junit" % "4.7" % "test" withSources
		val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7-SNAPSHOT" % "test" withSources
		val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test" withSources
   }

   class ScalaFlowAtomPub(info: ProjectInfo) extends DefaultProject(info)
   {
   		override def includeTest(s: String) = { s.endsWith("Spec") || s.endsWith("Specification") || s.endsWith("Unit")  }			
   }

}