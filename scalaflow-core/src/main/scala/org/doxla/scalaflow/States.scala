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

trait Contextual[T] {
  private[this] var ctx: List[T] = Nil

  def add(thing: T) = {
    ctx = thing :: ctx
  }

  def reset() = {
    ctx = Nil
  }

  def context(): List[T] = {
    ctx
  }

}


class WF {
  object state {
    def apply(name: Symbol)(transitionsBlock: => Unit) = {
      transitionsBlock
      new FState(name, event.context)
    }
  }

  object event extends Contextual[FlowTransition]{
    def apply(name: Symbol) = {
      new FEvent(name)
    }
  }
}

object test {

  new WF() {
    state('start) {
      event('happen)
    }
  }

}
