package org.doxla.scalaflow.engine

import org.doxla.scalaflow.ScalaFlow
import org.doxla.scalaflow.exception.InvalidWorkflowException
import org.doxla.scalaflow.component.{FlowTransition, FlowEvent}

class WorkflowInstance(val workflowDef: ScalaFlow)
        extends SymbolicAliases
        with PrettyToString {
  
  private[this] var currentFlowState = workflowDef.states.head

  def currentState = currentFlowState
  def currentStateName = currentState.name

  def availableTransitions = currentState.events.map { _.name }
  
  def transitionOn(eventName: Symbol) = {
    val matchingEvent = findEventByName(eventName)
    currentFlowState = matchingEvent.getOrElse(DoNothingFlowEvent).transition.to
    matchingEvent.isDefined
  }

  def findEventByName(name: Symbol): Option[FlowEvent] =
    currentFlowState.events.find { _.name == name }

  object DoNothingFlowEvent extends FlowEvent(null, GoNowhereTransition)

  object GoNowhereTransition extends FlowTransition(null, null) {
    def to = currentFlowState
    def ->(name: Symbol) = throw new UnsupportedOperationException("This is a static transition")
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

  @throws(classOf[InvalidWorkflowException])
  def from(workflow: ScalaFlow) = apply(workflow)

  @throws(classOf[InvalidWorkflowException])
  def definedAs(workflow: ScalaFlow) = apply(workflow)

}

trait SymbolicAliases {
  self: WorkflowInstance =>

  def !? = availableTransitions

  def ! = transitionOn _
}

trait PrettyToString {
  self: WorkflowInstance =>


  override def toString = "WorkflowInstance currently in " + currentStateName.name
}

sealed case class StateTransition(from: Symbol, event: Symbol, to: Symbol)

trait EventNotification {
	type EventHandler = PartialFunction[StateTransition, Boolean]
	
	private[this] val defaultEventHandler: EventHandler = { case _ => true }
	private[this] var eventHandler: Option[EventHandler] = None
	
	def addEventHandler(handler: EventHandler): Unit = eventHandler match {
		case None => eventHandler = Some(handler)
		case Some(e) => eventHandler = Some(e orElse handler)
	}
	
	def publish(event: StateTransition): Boolean = handle(event)
	
	private[this] def handle: EventHandler = eventHandler match {
		case None => defaultEventHandler
		case Some(e) => e orElse defaultEventHandler		
	}
}
