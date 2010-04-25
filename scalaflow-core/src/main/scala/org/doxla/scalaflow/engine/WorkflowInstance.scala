package org.doxla.scalaflow.engine

import org.doxla.scalaflow.ScalaFlow
import org.doxla.scalaflow.exception.InvalidWorkflowException
import collection.immutable.HashSet
import org.doxla.scalaflow.component.{FlowTransition, FlowEvent}

class WorkflowInstance(val workflowDef: ScalaFlow) extends SymbolicAliases {
  private[this] var currentFlowState = workflowDef.states.head

  def currentState = currentFlowState
  def currentStateName = currentState.name

  def availableTransitions = currentState.events.map { _.name }
  def transitionOn(event: Symbol) = {
    val matchingEvent = currentFlowState.events.find { e: FlowEvent =>
        e.name == event
    }
    if(matchingEvent.isDefined) {
      val to: Option[FlowTransition] = matchingEvent.get.to
      currentFlowState = to.getOrElse(GoNowhereTransition).state
      true
    }else
      false

  }

  object GoNowhereTransition extends FlowTransition(null, null) {
    override def state = currentFlowState
  }
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