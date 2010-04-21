package org.doxla.scalaflow.engine

import org.doxla.scalaflow.ScalaFlow
import org.doxla.scalaflow.exception.InvalidWorkflowException

class WorkflowInstance

object WorkflowInstance {

  @throws(classOf[InvalidWorkflowException])
  def apply(workflow: ScalaFlow) {
    workflow.states match {
      case Nil => throw new InvalidWorkflowException("No states have been defined")
      case _ => new WorkflowInstance
    }

  }

}