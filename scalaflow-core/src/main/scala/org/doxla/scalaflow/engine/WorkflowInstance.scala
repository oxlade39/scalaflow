package org.doxla.scalaflow.engine

import org.doxla.scalaflow.ScalaFlow
import org.doxla.scalaflow.exception.InvalidWorkflowException

class WorkflowInstance(val workflowDef: ScalaFlow) {
  private[this] var currentFlowState = workflowDef.states.head

  def currentState = currentFlowState
  def currentStateName = currentState.name
}

object WorkflowInstance {

  @throws(classOf[InvalidWorkflowException])
  def apply(workflow: ScalaFlow): WorkflowInstance = {
    workflow.states match {
      case Nil => throw new InvalidWorkflowException("No states have been defined")
      case _ => new WorkflowInstance(workflow)
    }

  }

}