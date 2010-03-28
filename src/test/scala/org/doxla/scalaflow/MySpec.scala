package org.doxla.scalaflow

import org.specs.Specification
import org.specs.runner.{JUnit4, ScalaTestSuite, ConsoleRunner}

class MySpecTest extends JUnit4(MySpec)
class MySpecSuite extends ScalaTestSuite(MySpec)

object MySpecRunner extends ConsoleRunner(MySpec)

object MySpec extends Specification {
  "A Workflow" should {
    "have a name" in {
      new ScalaFlow("Coffee Flow").name must be("Coffee Flow")
      new ScalaFlow("Another flow").name must be("Another flow")
    }

    "have states" in {
      new ScalaFlow("name") {
        state('stateUno)
      }.states.size must be(1)

      new ScalaFlow("name") {
        state('stateUno)
        state('stateDos)
      }.states.size must be(2)
    }

    "have ordered states" in {
      val statesList = new ScalaFlow("name") {
        state('stateUno)
        state('stateDos)
      }.states

      statesList.head.name must be('stateUno)
      statesList.tail.head.name must be('stateDos)      
    }

//    "have states with events" in {
//      new ScalaFlow("coffee flow") {
//        state('new) {
//          event('order)
//        }
//      }
//    }
  }
}
