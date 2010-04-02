package org.doxla.scalaflow.component


trait FlowDefinition {
  private[this] var flowStates: List[FlowState] = Nil
  private[this] var currentFlowEvents: List[FlowEvent] = Nil

  def states = flowStates.reverse

  def resetEvents = {
    currentFlowEvents = Nil
    this
  }

  def currentEvents = {
    val events = currentFlowEvents
    resetEvents
    events
  }

  def addEvent(eventName: Symbol) = {
    val flowEvent: FlowEvent = new FlowEvent(this, eventName)
    currentFlowEvents = flowEvent :: currentFlowEvents
    flowEvent
  }

  def addState(newState: FlowState) = {
    flowStates = newState :: flowStates
    newState
  }

  def findState(stateName: Symbol) = states.find {
    case FlowState(matchState, _) => matchState == stateName
  }
}