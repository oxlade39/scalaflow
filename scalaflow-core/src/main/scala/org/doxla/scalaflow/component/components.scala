package org.doxla.scalaflow.component

case class FlowState(name: Symbol, evts: List[FlowEvent]) {
  def events: List[FlowEvent] = evts.reverse
}
object StartState extends FlowState('start, Nil)

case class FlowEvent(name: Symbol, transition: FlowTransition)

abstract class FlowTransition(val name: Symbol, val from: Option[FlowState]) {
  def to: FlowState
  def ->(name: Symbol): FlowTransition
}

object NoTransition extends FlowTransition(null,null) {
  def to = throw new UnsupportedOperationException("NoTransitions don't have transitions")
  def ->(name: Symbol) = throw new UnsupportedOperationException("NoTransitions don't have transitions")  
}