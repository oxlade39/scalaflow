package org.doxla.scalaflow

import component.{FlowEvent, FlowTransition, FlowDefinition, FlowState}

trait States {
  self: ScalaFlow =>

  def states = flowDefinition.states

  def state(stateName: Symbol)(block: => Unit) {
    val stateBuilder = new StateBuilder(flowDefinition).withName(stateName)
    flowDefinition.addState(stateBuilder.build(block))
  }
  def endstate(newState: FlowState) = flowDefinition.addState(newState)
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

case class FState(name: Symbol, transitions: List[FlowTransition])

case class FEvent(name: Symbol) {
  def ->(stateName: Symbol) = {

  }
}

object State {

  var transitions: List[FlowTransition] = Nil
  var current: FState = Nothing

  def apply(name: Symbol)(transitionsBlock: () => Unit) = {
    transitionsBlock
    current = new FState(name, transitions)
    current
  }
}

object Event {
  def apply(name: Symbol) = {
    new FEvent(name)
  }
}

object test {

  State('start) {
    Event('choose_coffee) -> 'waiting
    Event('leave) -> 'done
  }

}
