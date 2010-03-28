package org.doxla.scalaflow

class ScalaFlow(val name: String)
        extends ScalaFlowImplicits
        with States
        with Events {

  protected[this] object flowDefinition extends FlowDefinition

}

trait States {
  self: ScalaFlow =>

  def states = flowDefinition.states

  def state(stateName: Symbol)(block: => Unit) {
    val stateBuilder = new StateBuilder(flowDefinition).withName(stateName)
    flowDefinition.addState(stateBuilder.build(block))
  }
  def endstate(newState: FlowState) = flowDefinition.addState(newState)
}

trait Events {
  self: ScalaFlow =>

  def event(newEvent: FlowEvent) = {
    flowDefinition.addEvent(newEvent)
    newEvent
  }
}

trait ScalaFlowImplicits {
  implicit def symbol2Event(name: Symbol) = new FlowEvent(name)
  implicit def symbol2State(name: Symbol) = new FlowState(name, Nil)
  //  implicit def symbol2StateBuilder(name: Symbol) = new StateBuilder().withName(name)
  //  implicit def stateBuilderToState(builder: StateBuilder) = builder.build
  //  implicit def symbolAndBlock2Event(name: Symbol)(block: => FlowEvent) = new StateBuilder().withName(name).withEvents()
}

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

  def addEvent(event: FlowEvent) = {
    currentFlowEvents = event :: currentFlowEvents 
  }

  def addState(newState: FlowState) = {
    flowStates = newState :: flowStates
    newState
  }
}

class StateBuilder(val ctx: FlowDefinition) {
  private[this] var name: Symbol = null

  def withName(name: Symbol) = {
    this.name = name
    this
  }

  def build(block: => Unit) = {
    ctx.resetEvents
    block
    new FlowState(name, ctx.currentEvents)
  }
}

class FlowState(val name: Symbol, val events: List[FlowEvent])
class FlowEvent(val name: Symbol)
