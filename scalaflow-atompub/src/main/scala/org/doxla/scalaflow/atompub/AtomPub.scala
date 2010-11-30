package org.doxla.scalaflow.atompub

import org.doxla.scalaflow._
import component.{FlowEvent, FlowState, FlowTransition}
import scala.xml._

case class AtomPub(scalaFlow: ScalaFlow, configuration: AtomPubConfiguration) {
	def serviceDocument: NodeSeq  = 
	<service xmlns="http://www.w3.org/2007/app" xmlns:atom="http://www.w3.org/2005/Atom">
	  <workspace>
	    <atom:title>{ scalaFlow.name }</atom:title>
		{ collections }
	  </workspace>	
	</service>
	
	def collections: NodeSeq = for(state <- scalaFlow.states) yield <collection href={collectionHref(state)}></collection>
	def collectionHref(state: FlowState): String = configuration.url + state.name.name.toLowerCase
}

case class AtomPubConfiguration(url: String)