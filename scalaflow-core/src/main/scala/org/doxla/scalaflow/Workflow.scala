package org.doxla.scalaflow

import component.{FlowEvent, FlowState, FlowTransition}

class ScalaFlow(val name: String) {

  private var flowStates: List[FlowState] = Nil
  def states = flowStates.reverse

  object state {
    def apply(name: Symbol)(transitionsBlock: => Unit) = {
      transitionsBlock
      flowStates = new FlowState(name, event.context) :: flowStates
      flowStates.head
    }
  }

  def endstate(name: Symbol) = state.apply(name){}

  private def flowStatesHead: Option[FlowState] = 
    flowStates match {
      case Nil => None
      case _ => Some(flowStates.head)
    }

  object event extends Contextual[FlowEvent]{
    def apply(eventName: Symbol): FlowTransition = {

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
