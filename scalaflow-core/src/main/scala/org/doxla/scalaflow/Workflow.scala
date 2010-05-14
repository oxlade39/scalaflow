package org.doxla.scalaflow

import component.{FlowEvent, FlowState, FlowTransition}

class ScalaFlow(val name: String) {

  private var flowStates: List[FlowState] = Nil
  def states = flowStates.reverse

  private[this] var hasEndState = false

  private def safe_?[T](f: => T):T = {
    hasEndState match {
      case false => f
      case _ => throw new IllegalStateException("Can't add states after endstate");
    }
  }

  object state {
    def apply(name: Symbol)(transitionsBlock: => Unit) = safe_?[FlowState] {
      addState(name)(transitionsBlock)
    }
  }

  def endstate(name: Symbol) = {
    hasEndState = true
    addState(name){}
  }

  private[this] def addState(name: Symbol)(transitionsBlock: => Unit): FlowState = {
    transitionsBlock
    flowStates = new FlowState(name, event.context) :: flowStates
    flowStates.head
  }

  private def flowStatesHead: Option[FlowState] = 
    flowStates match {
      case Nil => None
      case _ => Some(flowStates.head)
    }

  object event extends Contextual[FlowEvent]{
    def apply(eventName: Symbol) = safe_?[FlowTransition] {
      addEvent(eventName)
    }

    private[this] def addEvent(eventName: Symbol) : FlowTransition = {
      object anonTransition extends FlowTransition(eventName, flowStatesHead) {
        private[this] var stateName: Symbol = null

        private[this] object stateFinder extends StateFinder {
          val workflow = ScalaFlow.this
        }

        def to: FlowState =
          stateFinder.findState(stateName).getOrElse( throw new IllegalStateException("There is no registered state by the name: "+stateName))

        def ->(name: Symbol) = {
          stateName = name
          this
        }
      }

      add(FlowEvent(eventName, anonTransition)).transition
    }
  }

}

trait StateFinder {
  val workflow: ScalaFlow
  def findState(stateName: Symbol): Option[FlowState] = {
    workflow.states.find {
      case FlowState(name, _) => stateName == name
    }
  }
}

trait ScalaFlowImplicits {
  implicit def symbol2State(name: Symbol) = new FlowState(name, Nil)
}
