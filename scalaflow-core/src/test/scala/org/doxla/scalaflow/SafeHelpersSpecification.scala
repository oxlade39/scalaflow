package org.doxla.scalaflow

import component.{NoTransition, FlowTransition, FlowState}
import org.specs.Specification
import org.specs.runner.JUnit4

class SafeHelpersSpecificationTest extends JUnit4(SafeHelpersSpecification)
object SafeHelpersSpecification extends Specification {

  "SafeHelpers" should {

    "not be #safeState_? given an end state" in {
      object testExample extends SafeHelpers {
        hasEndState = true
      }

      testExample.safeState_? { new FlowState('test, Nil) } must throwA[IllegalStateException]
    }

    "be #safeState_? when no end state" in {
      object testExample extends SafeHelpers {
        hasEndState = false
      }

      testExample.safeState_? { new FlowState('test, Nil) } must notBeNull      
    }

    "not be #safeEvent_? when not adding a state" in {
      object testExample extends SafeHelpers
      testExample.safeEvent_? { NoTransition } must throwA[IllegalStateException]
    }

    "be #safeEvent_? when adding a state by nesting inside a #safeState_?" in {
      object testExample extends SafeHelpers
      testExample.safeState_? {
        testExample.safeEvent_? { NoTransition }
        new FlowState('test, Nil)
      } must notBeNull
    }
  }

}