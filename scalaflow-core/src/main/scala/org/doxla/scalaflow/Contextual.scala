package org.doxla.scalaflow

trait Contextual[T] {
  private[this] var ctx: List[T] = Nil

  def add(thing: T) = {
    ctx = thing :: ctx
    thing
  }

  def reset = {
    ctx = Nil
  }

  def context: List[T] = {
    val currentContext = ctx
    reset
    currentContext
  }

}