package org.doxla.scalaflow

import component.{FlowState, FlowEvent}
import org.specs.Specification
import org.specs.specification.PendingUntilFixed
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers

class StateBuilderSpecificationTest extends JUnit4(StateBuilderSpecification)
object StateBuilderSpecification extends Specification with Mockito {

  type ContextualFlowEventFunc = Function0[Contextual[FlowEvent]]
  object ctx extends Contextual[FlowEvent]
  val testCtx: Contextual[FlowEvent] = ctx

  object testStateBuilder extends StateBuilder with SafeHelpers {    
    def event = ctx
    val stateRepository = mock[StateRepository]
  }

  "A StateBuilder" should {
    "add a state to the StateRepository" in {

      val flowEvent: FlowEvent = new FlowEvent('foom, null)
      testStateBuilder.event.add( flowEvent )
      var setWhenStateCalled = false

      val block = { () => setWhenStateCalled = true }

      testStateBuilder.state('symbol_name){ block }
			
      there was one(testStateBuilder.stateRepository).addState(Matchers.eq('symbol_name))( any[ContextualFlowEventFunc] )
    }
  }
}