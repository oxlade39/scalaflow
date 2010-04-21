package org.doxla.scalaflow.engine

import org.specs.runner.JUnit4
import org.specs.Specification
import org.doxla.scalaflow.{ScalaFlow, WorkflowSpecification}
import org.doxla.scalaflow.exception.InvalidWorkflowException


class WorkflowInstanceSpecificationTest extends JUnit4(WorkflowInstanceSpecification)
object WorkflowInstanceSpecification extends Specification {

  "A WorkflowInstance" should {
    "not except empty Workflows" in {
      WorkflowInstance(emptyWorkflow) must throwA[InvalidWorkflowException]
    }
  }

  object emptyWorkflow extends ScalaFlow("EMPTY")

}