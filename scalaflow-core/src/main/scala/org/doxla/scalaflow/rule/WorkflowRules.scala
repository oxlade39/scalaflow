package org.doxla.scalaflow.rule

import org.doxla.scalaflow.example.CustomerCoffeeWorkflow
import org.doxla.scalaflow.component.FlowState
import org.doxla.scalaflow.{ScalaFlow}

object Rule {
  def apply(stateName: Symbol, eventName: Symbol)(evaluateFunc: => Boolean): Rule = {
    new Rule {
      val state = stateName
      val event = eventName


      def evaluate = evaluateFunc
    }
  }
}

trait Rule {
  val state: Symbol
  val event: Symbol

  def evaluate: Boolean
}

trait TrueRule extends Rule {
  def evaluate = true
}

case class WorkflowRules(workflow: ScalaFlow, rules: List[Rule]) {
  rules.foreach { rule: Rule =>
    val matchingState = workflow.states.find {
      case FlowState(name, _) => rule.state == name
    }
    val events = matchingState.getOrElse(throw new IllegalArgumentException("There is no state named "+rule.state+" in workflow "+workflow)).events
    if(!events.map{_.name}.contains(rule.event))
      throw new IllegalArgumentException("There is no event named "+rule.event+" in FlowState "+matchingState.get)
  }

}


object test {

  object StartRule extends Rule {
    def evaluate = false

    val state = 'start
    val event = 'Place_Order
  }

  val rules = WorkflowRules(CustomerCoffeeWorkflow, List(StartRule))

}
