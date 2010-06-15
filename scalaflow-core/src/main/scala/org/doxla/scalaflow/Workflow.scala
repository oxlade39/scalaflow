package org.doxla.scalaflow

import component.{FlowEvent, FlowState, FlowTransition}

class ScalaFlow(val name: String)
      extends SafeHelpers
      with StateRepositoryProvider
      with StateBuilder
      with EventBuilder {

  def states = stateRepository.states

}

trait SafeHelpers {
  private[scalaflow] var hasEndState = false
  private[this] var addingState = false

  private[scalaflow] def safeState_?(f: => FlowState):FlowState = {
    hasEndState match {
      case false => addingState{ f }
      case _ => throw new IllegalStateException("Can not add states after endstate");
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

trait StateBuilder { self: SafeHelpers =>

  def event: Contextual[FlowEvent]
  val stateRepository: StateRepository

  object state {
    def apply(name: Symbol)(transitionsBlock: => Unit) = safeState_? {
      stateRepository.addState(name){ () =>
        transitionsBlock
        event
      }
    }
  }

  def endstate(name: Symbol) = {
    hasEndState = true
    stateRepository.addState(name){ () =>
      event
    }
  }

}

trait StateRepositoryProvider {
  val stateRepository = new StateRepository
}

class StateRepository {
  private[this] var internalStates: List[FlowState] = Nil

  def states = internalStates.reverse

  def findState(stateName: Symbol): Option[FlowState] = {
    states.find {
      case FlowState(name, _) => stateName == name
    }
  }

  def statesHead: Option[FlowState] = internalStates match {
    case Nil => None
    case _ => Some(states.head)
  }

  def addState(name: Symbol)(transitionsBlock: () => Contextual[FlowEvent]): FlowState = {
    val event = transitionsBlock()
    internalStates = new FlowState(name, event.context) :: internalStates
    internalStates.head
  }
}

trait EventBuilder { self: SafeHelpers =>

  val stateRepository: StateRepository

  object event extends Contextual[FlowEvent] {
    def apply(eventName: Symbol) = safeEvent_? {
      addEvent(eventName)
    }

    private[this] def addEvent(eventName: Symbol) : FlowTransition = {
      object anonTransition extends FlowTransition(eventName, stateRepository.statesHead) {
        private[this] var stateName: Symbol = null

        def to: FlowState =
          stateRepository.findState(stateName).getOrElse(
                          throw new IllegalStateException("There is no registered state by the name: "+stateName))

        def ->(name: Symbol) = {
          stateName = name
          this
        }
      }

      add(FlowEvent(eventName, anonTransition)).transition
    }
  }
}
