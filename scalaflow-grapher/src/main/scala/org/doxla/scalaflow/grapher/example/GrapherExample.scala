package org.doxla.scalaflow.grapher.graphviz.example

import org.doxla.scalaflow.example.CustomerCoffeeWorkflow
import org.doxla.scalaflow.grapher.graphviz.ScalaFlowGraphvizRenderer
import java.io.{PrintWriter, File}
import org.doxla.scalaflow.grapher.ScalaFlowGrapher


object GrapherExample {
  def main(args: Array[String]) = {
    val renderer = new ScalaFlowGraphvizRenderer()
    val printWriter = new PrintWriter(new File("testoutput.dot"), "UTF-8")
    renderer.setOut(printWriter)
    renderer.setRankdir("TB")

    new ScalaFlowGrapher(renderer).of(CustomerCoffeeWorkflow).graph
  }  
}