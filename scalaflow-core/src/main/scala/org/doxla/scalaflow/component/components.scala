package org.doxla.scalaflow.component

case class FlowState(name: Symbol, evts: List[FlowEvent]) {
  def events: List[FlowEvent] = evts.reverse
}

class FlowEvent(val context: FlowDefinition, val name: Symbol) {
  var to: Option[FlowTransition] = None

  def transitionsTo: FlowTransition = to.getOrElse( NoTransition )

  def ->(stateName: Symbol) = transitionsTo(new FlowTransition(context, stateName))

  def transitionsTo(to: FlowTransition) {
    this.to = Some(to)
  }
}

class FlowTransition(private[this] val ctx: FlowDefinition, private[this] val stateName: Symbol) {
  def state: FlowState = {
    ctx.findState(stateName).getOrElse( throw new IllegalStateException("There is no state with name: " +stateName+ "but a transition to it has been defined") )
  }
}

object NoTransition extends FlowTransition(null,null) {
  override def state = throw new UnsupportedOperationException("NoTransitions don't have transitions")
}