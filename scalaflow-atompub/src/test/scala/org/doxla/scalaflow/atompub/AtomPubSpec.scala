package org.doxla.scalaflow.atompub

import org.specs.Specification
import org.specs.specification.PendingUntilFixed
import org.doxla.scalaflow.example._

object AtomPubSpec extends Specification with PendingUntilFixed {

  "AtomPub" should {
	
	val underTest = AtomPub(CustomerCoffeeWorkflow, AtomPubConfiguration("http://localhost:8080/example/"))

	"generate a service document with workspace title from ScalaFlow" in {
		val serviceDoc = underTest.serviceDocument
		(serviceDoc \ "workspace" \ "title").text mustEqual "Customer Coffee"
	}
	
	"generate a service document with a collection for each state" in {
		val serviceDoc = underTest.serviceDocument
		(serviceDoc \ "workspace" \ "collection").size mustEqual CustomerCoffeeWorkflow.states.size
	}
	
	"generate a service document with collection hrefs using state name" in {
		val serviceDoc = underTest.serviceDocument
		((serviceDoc \ "workspace" \ "collection" ).head \ "@href" text) mustEqual "http://localhost:8080/example/start"
		((serviceDoc \ "workspace" \ "collection" ).tail.head \ "@href" text) mustEqual "http://localhost:8080/example/order_placed"
	}
	
    "generate a service document from ScalaFlow" in {
		underTest.serviceDocument mustEqual <service xmlns="http://www.w3.org/2007/app" xmlns:atom="http://www.w3.org/2005/Atom">
		  <workspace>
		    <atom:title>Customer Coffee Workflow</atom:title>
		    <collection href="http://localhost:8080/example/start" >
		      <atom:title>start</atom:title>
		      <accept>text/xml</accept>
			  <accept>application/atom+xml</accept>
			  <accept>application/xml</accept>		
		    </collection>
		    <collection href="http://localhost:8080/example/order_placed" >
		      <atom:title>Order_Placed</atom:title>
		      <accept>text/xml</accept>
			  <accept>application/atom+xml</accept>
			  <accept>application/xml</accept>
		    </collection>
		    <collection href="http://localhost:8080/example/order_updated" >
		      <atom:title>Order_Updated</atom:title>
		      <accept>text/xml</accept>
			  <accept>application/atom+xml</accept>
			  <accept>application/xml</accept>
		    </collection>
		    <collection href="http://localhost:8080/example/paid" >
		      <atom:title>Paid</atom:title>
		      <accept>text/xml</accept>
			  <accept>application/atom+xml</accept>
			  <accept>application/xml</accept>
		    </collection>
		    <collection href="http://localhost:8080/example/drink_recieved" >
		      <atom:title>Drink_Recieved</atom:title>
		      <accept>text/xml</accept>
			  <accept>application/atom+xml</accept>
			  <accept>application/xml</accept>
		    </collection>
		    <collection href="http://localhost:8080/example/drink" >
		      <atom:title>Drink</atom:title>
		      <accept>text/xml</accept>
			  <accept>application/atom+xml</accept>
			  <accept>application/xml</accept>
		    </collection>		
		  </workspace>		
		</service>
	} pendingUntilFixed
  }
}
