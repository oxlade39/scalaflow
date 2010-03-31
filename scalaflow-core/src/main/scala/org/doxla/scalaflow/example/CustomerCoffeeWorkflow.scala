package org.doxla.scalaflow.example

import org.doxla.scalaflow.ScalaFlow

/**
 * ScalaFlow definition for Customer Coffee workflow from:
 * http://www.infoq.com/articles/webber-rest-workflow
 */
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