package org.doxla.scalaflow.rule

import org.specs.Specification
import org.specs.runner.JUnit4
import org.doxla.scalaflow.example.CustomerCoffeeWorkflow


class WorkflowRulesSpecificationTest extends JUnit4(WorkflowRulesSpecification)
object WorkflowRulesSpecification extends Specification {

  def TrueRule(stateName: Symbol, eventName: Symbol): Rule = Rule(stateName, eventName)(true)

  "A WorkflowRules" should {
    "Allow a list of rules where states and events match" in {
      WorkflowRules(CustomerCoffeeWorkflow, List(TrueRule('start, 'Place_Order))) must notBeNull
    }

    "Not allow a list of rules for state names that do not exist" in {
      WorkflowRules(CustomerCoffeeWorkflow, List(TrueRule('bad_state, 'Place_Order))) must throwA[IllegalArgumentException]
    }

    "Not allow a list of rules for event names that do not exist" in {
      WorkflowRules(CustomerCoffeeWorkflow, List(TrueRule('start, 'Bad_Event))) must throwA[IllegalArgumentException]
    }
  }
}