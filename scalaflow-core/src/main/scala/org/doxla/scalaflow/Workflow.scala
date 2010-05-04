package org.doxla.scalaflow

import component.{StartState, FlowEvent, FlowState, FlowTransition}

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
      add(new FlowEvent(eventName, new FlowTransition(eventName, flowStatesHead){
        private[this] var stateName: Symbol = null

        def to: FlowState = {
          flowStates.find {
            case FlowState(name, _) => stateName == name
          }.getOrElse( throw new IllegalStateException("There is no registered state by the name: "+stateName))
        }

        def ->(name: Symbol) = {
          println("adding stateName "+name)
          stateName = name
          this
        }
      })).transition
    }
  }

}

trait ScalaFlowImplicits {
  implicit def symbol2State(name: Symbol) = new FlowState(name, Nil)
}
