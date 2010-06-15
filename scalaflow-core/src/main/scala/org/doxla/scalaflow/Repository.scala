package org.doxla.scalaflow

import component.{FlowEvent, FlowState}

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

trait StateRepositoryProvider {
  val stateRepository = new StateRepository
}