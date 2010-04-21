package org.doxla.scalaflow.engine

import org.specs.runner.JUnit4
import org.specs.Specification
import org.doxla.scalaflow.{ScalaFlow, WorkflowSpecification}
import org.doxla.scalaflow.exception.InvalidWorkflowException
import org.doxla.scalaflow.example.CustomerCoffeeWorkflow


class WorkflowInstanceSpecificationTest extends JUnit4(WorkflowInstanceSpecification)
object WorkflowInstanceSpecification extends Specification {

  "A WorkflowInstance" should {
    "not except empty ScalaFlows" in {
      WorkflowInstance(emptyWorkflow) must throwA[InvalidWorkflowException]
    }

    "accept non empty ScalaFlows" in {
      WorkflowInstance(CustomerCoffeeWorkflow) must notBeNull
    }

    "begin in the first state of a ScalaFlow" in {
      WorkflowInstance(CustomerCoffeeWorkflow).currentState mustBe(CustomerCoffeeWorkflow.states.head)
    }

    "tell you the current state" in {
      WorkflowInstance(CustomerCoffeeWorkflow).currentStateName mustBe('start)
    }

    "tell you the available transitions for current state" in {
      WorkflowInstance(CustomerCoffeeWorkflow).availableTransitions mustEqual('Place_Order :: Nil)
      WorkflowInstance(CustomerCoffeeWorkflow).!? mustEqual('Place_Order :: Nil)
    }

  }

  object emptyWorkflow extends ScalaFlow("EMPTY")

}