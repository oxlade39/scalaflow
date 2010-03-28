package org.doxla.scalaflow

class ScalaFlow(val name: String)
        extends ScalaFlowImplicits
        with StateBuilders
        with EventBuilders {

  protected[this] object flowDefinition extends FlowDefinition

}

trait StateBuilders {
  self: ScalaFlow =>

  def states = flowDefinition.states

  def state(newState: FlowState)(block: => FlowEvent) = flowDefinition.addState(newState)
  def endstate(newState: FlowState) = flowDefinition.addState(newState)
}

trait EventBuilders {
  self: ScalaFlow =>

  def event(newEvent: FlowEvent) = newEvent
}

trait ScalaFlowImplicits {
  implicit def symbol2State(name: Symbol) = new FlowState(name)
  implicit def symbol2Event(name: Symbol) = new FlowEvent(name)
}

trait FlowDefinition {
  private[this] var flowStates: List[FlowState] = Nil

  def states = flowStates.reverse

  def addState(newState: FlowState) = {
    flowStates = newState :: flowStates
    newState
  }
}

class FlowState(val name: Symbol)
class FlowEvent(val name: Symbol)
