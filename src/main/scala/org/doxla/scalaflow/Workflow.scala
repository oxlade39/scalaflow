package org.doxla.scalaflow

class ScalaFlow(val name: String) {

  private[this] object flowDefinition extends FlowDefinition

  def state(newState: FlowState) = flowDefinition.addState(newState)
  
  def states = flowDefinition.states

  implicit def symbol2State(name: Symbol) = new FlowState(name)

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
