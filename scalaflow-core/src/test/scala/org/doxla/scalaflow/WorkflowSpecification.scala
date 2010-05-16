package org.doxla.scalaflow

import org.specs.Specification
import org.specs.runner.{JUnit4, ScalaTestSuite, ConsoleRunner}

class WorkflowSpecificationTest extends JUnit4(WorkflowSpecification)
object WorkflowSpecification extends Specification {

  "A Workflow" should {
    "have a name" in {
      new ScalaFlow("Coffee Flow").name must be("Coffee Flow")
      new ScalaFlow("Another flow").name must be("Another flow")
    }

    "have states" in {
      new ScalaFlow("name") {
        endstate('stateUno)
      }.states.size must be(1)

      new ScalaFlow("name") {
        endstate('stateUno)
        endstate('stateDos)
      }.states.size must be(2)
    }

    "have ordered states" in {
      val statesList = new ScalaFlow("name") {
        endstate('stateUno)
        endstate('stateDos)
      }.states

      statesList.head.name must be('stateUno)
      statesList.tail.head.name must be('stateDos)      
    }

    "have states with events" in {
      new ScalaFlow("coffee flow") {
        state('new) {
          event('order)
        }
      }.states.head.events.size must be(1)
    }

    "have states with ordered events" in {
      val events = new ScalaFlow("coffee flow") {
        state('new) {
          event('order)
          event('leave)
        }
      }.states.head.events

      events.size must be(2)
      events.head.name must be('order)
      events.tail.head.name must be('leave)
    }

    "not allow new states to be added after an endstate is added" in {
      object FinalWorkflow extends ScalaFlow("Workflow with endstate") {
        endstate('end)
      }
      FinalWorkflow.state('another){} must throwA[IllegalStateException]
    }

    "allow new endstates to be added after other endstates are added" in {
      object FinalWorkflow extends ScalaFlow("Workflow with endstate") {
        endstate('end)
      }
      FinalWorkflow.endstate('another)
      FinalWorkflow.states.size must be(2)
    }
  }

  "Events" should {
    "have transitions" in {
      object CoffeeFlow extends ScalaFlow("coffee flow") {
        state('new) {
          event('order) -> 'pay
        }
        endstate('pay)
      }
      val newState = CoffeeFlow.states.head
      val payState = CoffeeFlow.states.tail.head
      
      newState.events.head.transition.to must be(payState)
    }

    "not be added outside of a state" in {
      new ScalaFlow("with endstate") {
        event('order) -> 'end
      } must throwA[IllegalStateException]
    }
  }
}
