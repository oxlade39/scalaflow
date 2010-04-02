package org.doxla.scalaflow.grapher

import graphviz.{LabeledGraphvizEdge}
import com.google.inject.grapher.graphviz.{EdgeStyle, NodeShape, GraphvizNode, GraphvizRenderer}
import org.doxla.scalaflow.component.{FlowEvent, FlowState}
import org.doxla.scalaflow.ScalaFlow

class ScalaFlowGrapher(renderer: GraphvizRenderer) {
  
  implicit def flowState2GraphvizNode(state: FlowState) = {
    val graphvizNode: GraphvizNode = new GraphvizNode(state.name.name)
    graphvizNode.setTitle(state.name.name.replaceAll("_", " "))
    graphvizNode.setShape(NodeShape.ELLIPSE)
    graphvizNode
  }

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