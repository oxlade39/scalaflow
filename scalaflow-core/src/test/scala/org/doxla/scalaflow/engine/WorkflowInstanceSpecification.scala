package org.doxla.scalaflow.engine

import org.specs.runner.JUnit4
import org.specs.Specification
import org.doxla.scalaflow.{ScalaFlow}
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
    }

    "accept transitions on available events" in {
      WorkflowInstance(CustomerCoffeeWorkflow) transitionOn 'Place_Order mustBe(true)
    }

    "reject transitions on non existant events" in {
      WorkflowInstance(CustomerCoffeeWorkflow) transitionOn 'Bad_Event mustBe(false)
    }

    "move states on events" in {
      val wi = WorkflowInstance(CustomerCoffeeWorkflow)
      wi transitionOn 'Place_Order
      wi.availableTransitions mustEqual('Pay :: 'Update_Order :: Nil)
    }

  }

  "A WorkflowInstance with SymbolicAliases" should {
    "have a shortened availableTransitions method" in {
      WorkflowInstance(CustomerCoffeeWorkflow).!? mustEqual('Place_Order :: Nil)
    }

    "have a shortened transitionOn method for positive case" in {
      WorkflowInstance(CustomerCoffeeWorkflow) ! 'Place_Order mustBe(true)
    }

    "have a shortened transitionOn method for negative case" in {
      WorkflowInstance(CustomerCoffeeWorkflow) ! 'Bad_Event mustBe(false)
    }
  }

  object emptyWorkflow extends ScalaFlow("EMPTY")

}