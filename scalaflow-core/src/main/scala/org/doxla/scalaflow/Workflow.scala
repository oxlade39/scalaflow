package org.doxla.scalaflow

import component.{FlowDefinition, FlowEvent, FlowState}
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


