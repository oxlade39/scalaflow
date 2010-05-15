package org.doxla.scalaflow

import component.{FlowEvent, FlowState, FlowTransition}

class ScalaFlow(val name: String) extends SafeHelpers {

  private var flowStates: List[FlowState] = Nil
  def states = flowStates.reverse

  object state {
    def apply(name: Symbol)(transitionsBlock: => Unit) = safeState_? {
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
    def apply(eventName: Symbol) = safeEvent_? {
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

trait SafeHelpers {
  private[scalaflow] var hasEndState = false
  private[this] var addingState = false

  private[scalaflow] def safeState_?(f: => FlowState):FlowState = {
    hasEndState match {
      case false => addingState{ f }
      case _ => throw new IllegalStateException("Can't add states after endstate");
    }
  }

  private[scalaflow] def safeEvent_?(f: => FlowTransition): FlowTransition = {
    addingState match {
      case true => f
      case _ => throw new IllegalStateException("Can not add events outside of states")
    }
  }

  private[this] def addingState(f: => FlowState): FlowState = {
    addingState = true
    try{
      f
    }finally{
      addingState = false
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
