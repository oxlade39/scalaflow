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