package org.doxla.scalaflow

import scala.Option

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

  def event(newEventName: Symbol) = {
    flowDefinition.addEvent(newEventName)
  }
}

trait ScalaFlowImplicits {
  implicit def symbol2State(name: Symbol) = new FlowState(name, Nil)
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

case class FlowState(name: Symbol, evts: List[FlowEvent]) {
  def events: List[FlowEvent] = evts.reverse
}
class FlowEvent(val context: FlowDefinition, val name: Symbol) {
  var to: Option[FlowTransition] = None

  def transitionsTo: FlowTransition = {
    to.getOrElse( NoTransition )
  }

  def transitionsTo(to: FlowTransition) {
    this.to = Some(to)
  }

  def ->(stateName: Symbol) = {
    transitionsTo(new FlowTransition(context, stateName))
  }
}

class FlowTransition(private[this] val ctx: FlowDefinition, private[this] val stateName: Symbol) {
  def state = {
    ctx.findState(stateName).getOrElse( throw new IllegalStateException("There is no state with name: " +stateName+ "but a transition to it has been defined") )
  }
}
object NoTransition extends FlowTransition(null,null) {
  override def state = throw new UnsupportedOperationException("NoTransitions don't have transitions")
}
