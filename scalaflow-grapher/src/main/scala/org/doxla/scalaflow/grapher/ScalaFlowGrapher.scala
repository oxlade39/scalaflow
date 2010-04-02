package org.doxla.scalaflow.grapher

import graphviz.{ScalaGraphvizRenderer, LabeledGraphvizEdge}
import java.io.{PrintWriter, File}
import org.doxla.scalaflow.{FlowEvent, FlowState, ScalaFlow}
import com.google.inject.grapher.graphviz._
import java.lang.String
import org.doxla.scalaflow.example.CustomerCoffeeWorkflow

class ScalaFlowGrapher(renderer: GraphvizRenderer) {
  implicit def flowState2GraphvizNode(state: FlowState) = {
    val graphvizNode: GraphvizNode = new GraphvizNode(state.name.name)
    graphvizNode.setTitle(state.name.name.replaceAll("_", " "))
    graphvizNode.setShape(NodeShape.ELLIPSE)
    graphvizNode
  }

  //  implicit def flowEvent2GrapizEdge(event: FlowEvent) = {
  //    val edge = new GraphvizEdge(event.transitionsTo.)
  //  }

  var flowToGraph: Option[ScalaFlow] = None

  def flow: ScalaFlow = flowToGraph.getOrElse(throw new IllegalStateException("please set the ScalaFlow to take a graph 'of'"))

  def of(toGraph: ScalaFlow) = {
    flowToGraph = Some(toGraph)
    this
  }

  def graph() {
    for (state: FlowState <- flow.states) {
      renderer.addNode(state)
      for (event: FlowEvent <- state.events) {
        val edge = new LabeledGraphvizEdge(state.name.name, event.transitionsTo.state.name.name)
        edge.setStyle(EdgeStyle.SOLID)
        edge.setLabel(event.name.name)
        renderer.addEdge(edge)
      }
    }
    renderer.render
  }

}