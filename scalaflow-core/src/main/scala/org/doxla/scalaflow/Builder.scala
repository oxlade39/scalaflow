package org.doxla.scalaflow

import component.{FlowTransition, FlowState, FlowEvent}

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