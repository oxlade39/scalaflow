package org.doxla.scalaflow.engine

import org.doxla.scalaflow.ScalaFlow
import org.doxla.scalaflow.exception.InvalidWorkflowException
import org.doxla.scalaflow.component.FlowEvent
import collection.immutable.HashSet

class WorkflowInstance(val workflowDef: ScalaFlow) extends SymbolicAliases {
  private[this] var currentFlowState = workflowDef.states.head

  def currentState = currentFlowState
  def currentStateName = currentState.name

  def availableTransitions = currentState.events.map { _.name }
  def transitionOn(event: Symbol) = availableTransitions.contains(event)
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

trait SymbolicAliases {
  self: WorkflowInstance =>

  def !? = availableTransitions

  def !(event: Symbol) = transitionOn(event)
}