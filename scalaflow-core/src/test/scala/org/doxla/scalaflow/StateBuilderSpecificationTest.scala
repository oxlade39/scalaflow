package org.doxla.scalaflow

import component.{FlowState, FlowEvent}
import org.specs.Specification
import org.specs.runner.JUnit4
import org.specs.mock.Mockito

class StateBuilderSpecificationTest extends JUnit4(StateBuilderSpecification)
object StateBuilderSpecification extends Specification with Mockito {

  object testStateBuilder extends StateBuilder {
    val ctx = new Contextual[FlowEvent]
    def event = ctx
    val stateRepository = mock[StateRepository]
  }

  "A StateBuilder" should {
    "add a state to the StateRepository" in {

      val flowEvent: FlowEvent = new FlowEvent('foom, null)
      testStateBuilder.event.add( flowEvent )
      var setWhenStateCalled = false

      val block = { setWhenStateCalled = true }

      testStateBuilder.state('symbol_name){ block }
      testStateBuilder.stateRepository.addState('symbol_name) {
        block
        flowEvent
      } was called
    }
  }
}