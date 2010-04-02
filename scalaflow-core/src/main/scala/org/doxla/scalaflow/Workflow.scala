package org.doxla.scalaflow

import component.{FlowDefinition, FlowState}

class ScalaFlow(val name: String) extends ScalaFlowImplicits
                                  with States
                                  with Events {
  
  protected[this] object flowDefinition extends FlowDefinition

}

trait ScalaFlowImplicits {
  implicit def symbol2State(name: Symbol) = new FlowState(name, Nil)
}
