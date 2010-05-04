package org.doxla.scalaflow.grapher.graphviz

import com.google.inject.grapher.graphviz.{GraphvizRenderer, GraphvizEdge}
import java.io.{PrintWriter, File}

class ScalaFlowGraphvizRenderer extends GraphvizRenderer {
  override def getEdgeAttributes(edge: GraphvizEdge) = {
    val attributes = super.getEdgeAttributes(edge)
    if (edge.isInstanceOf[LabeledGraphvizEdge] && edge.asInstanceOf[LabeledGraphvizEdge].hasLabel) {
      attributes.put("label", "<" + edge.asInstanceOf[LabeledGraphvizEdge].getLabel + ">")
    }
    attributes
  }

  def >>(fileName: String) = {
    setOut(new PrintWriter(new File(fileName), "UTF-8"))
    setRankdir("TB")
  }
}