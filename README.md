# ScalaFlow

An attempt to write a workflow engine that supports a natural language for workflow definition.

## Components

1. scalaflow-core
2. scalaflow-grapher
3. scalaflow-atompub

## Scalaflow-core

The core components for defining a *ScalaFlow*. Also includes the *WorkflowInstance* classes for using a *ScalaFlow* as seen below.

### Here's an example definition of a ScalaFlow
<pre><code>
object CustomerCoffeeWorkflow extends ScalaFlow("Customer Coffee") {
  state('start) {
    event('Place_Order) -> 'Order_Placed
  }
  state('Order_Placed) {
    event('Pay) -> 'Paid
    event('Update_Order) -> 'Order_Updated
  }
  state('Order_Updated) {
    event('Update_Accepted) -> 'Order_Placed
    event('Update_Rejected) -> 'Order_Placed
  }
  state('Paid) {
    event('Pickup) -> 'Drink_Received
  }
  state('Drink_Received) {
    event('leave) -> 'Drink
  }
  endstate('Drink)
}
</code></pre>

### Example usage from scala console:
<pre><code>
scala> val wi = WorkflowInstance definedAs CustomerCoffeeWorkflow
wi: org.doxla.scalaflow.engine.WorkflowInstance = WorkflowInstance currently in start

scala> wi !?
res11: List[Symbol] = List('Place_Order)

scala> wi ! 'Place_Order
res12: Boolean = true

scala> wi
res13: org.doxla.scalaflow.engine.WorkflowInstance = WorkflowInstance currently in Order_Placed

scala> wi !?
res14: List[Symbol] = List('Pay, 'Update_Order)

scala> wi ! 'Pay
res15: Boolean = true

scala> wi !?
res16: List[Symbol] = List('Pickup)

scala> wi
res17: org.doxla.scalaflow.engine.WorkflowInstance = WorkflowInstance currently in Paid
</code></pre>

## Scalaflow-grapher
A very basic extension of Google's [guice-grapher](http://code.google.com/p/google-guice/wiki/Grapher) module. To generate for cool visual representations of *ScalaFlow*s. Use [Graphviz viewer](http://www.graphviz.org/) to view the generated files.

### *CustomerCoffeeWorkflow* generated image
![CustomerCoffeeWorkflow generated image](scalaflow/raw/master/scalaflow-grapher/testoutput.jpg "CustomerCoffeeWorkflow")

## Scalaflow-atompub

I started playing with some ideas for generating [atompub](http://bitworking.org/projects/atom/rfc5023.html) XML documents. Still very much work in progress.