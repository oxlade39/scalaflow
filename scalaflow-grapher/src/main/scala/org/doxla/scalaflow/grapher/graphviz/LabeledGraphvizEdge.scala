package org.doxla.scalaflow.grapher.graphviz

import com.google.inject.grapher.graphviz.GraphvizEdge


class LabeledGraphvizEdge(val tailId: String, val headId: String) extends GraphvizEdge(tailId, headId) {
  private[this] var label: Option[String] = None

  def getLabel = label.get

  def setLabel(label: String) = this.label = Some(label.replaceAll("_", " "))

  def hasLabel = label.isDefined
}