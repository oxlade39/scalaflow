package org.doxla.scalaflow.grapher.graphviz

import com.google.inject.grapher.graphviz.{GraphvizRenderer, GraphvizEdge}

class ScalaGraphvizRenderer extends GraphvizRenderer {
  override def getEdgeAttributes(edge: GraphvizEdge) = {
    val attributes = super.getEdgeAttributes(edge)
    if (edge.isInstanceOf[LabeledGraphvizEdge] && edge.asInstanceOf[LabeledGraphvizEdge].hasLabel) {
      attributes.put("label", "<" + edge.asInstanceOf[LabeledGraphvizEdge].getLabel + ">")
    }
    attributes
  }
}