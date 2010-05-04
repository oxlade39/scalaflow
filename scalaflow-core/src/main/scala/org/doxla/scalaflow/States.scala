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

case class FState(name: Symbol, transitions: List[FTransition])

case class FEvent(name: Symbol) {
  def ->(stateName: Symbol) = {

  }
}

abstract class FTransition(val name: Symbol, val from: FState) {
private[this] var stateName: Symbol = null

  def to: Option[FState]
  def ->(name: Symbol): FTransition
}

trait Contextual[T] {
  private[this] var ctx: List[T] = Nil

  def add(thing: T) = {
    ctx = thing :: ctx
    thing
  }

  def reset = {
    ctx = Nil
  }

  def context: List[T] = {
    val currentContext = ctx
    reset
    currentContext
  }

}

class WF {

  private[this] var states: List[FState] = Nil

  object state {
    def apply(name: Symbol)(transitionsBlock: => Unit) = {
      transitionsBlock
      states = new FState(name, event.context) :: states
      states.head
    }
  }

  object event extends Contextual[FTransition]{
    def apply(eventName: Symbol) = {
      add(new FTransition(eventName, states.head){
        private[this] var stateName: Symbol = null

        def to: Option[FState] = {
          states.find {
            case FState(name, _) => stateName == name
          }
        }

        def ->(name: Symbol) = {
          stateName = name
          this
        }
      })
    }
  }
}

object test {

  new WF() {
    state('start) {
      event('happen) -> 'next
    }
  }

}
