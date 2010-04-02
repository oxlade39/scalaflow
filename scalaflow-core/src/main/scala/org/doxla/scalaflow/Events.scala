package org.doxla.scalaflow

trait Events {
  self: ScalaFlow =>

  def event(newEventName: Symbol) = {
    flowDefinition.addEvent(newEventName)
  }
}