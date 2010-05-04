package org.doxla.scalaflow

import component.{FlowEvent, FlowTransition, FlowState}

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

//class WF {
//
//  private[this] var states: List[FState] = Nil
//
//  object state {
//    def apply(name: Symbol)(transitionsBlock: => Unit) = {
//      transitionsBlock
//      states = new FState(name, event.context) :: states
//      states.head
//    }
//  }
//
//  object event extends Contextual[FTransition]{
//    def apply(eventName: Symbol) = {
//      add(new FTransition(eventName, states.head){
//        private[this] var stateName: Symbol = null
//
//        def to: Option[FState] = {
//          states.find {
//            case FState(name, _) => stateName == name
//          }
//        }
//
//        def ->(name: Symbol) = {
//          stateName = name
//          this
//        }
//      })
//    }
//  }
//}

//object test {
//
//  new WF() {
//    state('start) {
//      event('happen) -> 'next
//    }
//  }
//
//}
