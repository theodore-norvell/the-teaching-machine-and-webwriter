package ;

import js.Lib;
import js.Dom ;

/** Tutorials
 * @author Theodore Norvell
 */

class Main {

	static var tmProxy : TMProxy ;
	static var vertexStack : List<TutorialVertex> = new List<TutorialVertex>() ;
	
	// The following declaration is a trick to ensure that the EdgeFunctions class
	// is linked in.
	static var neverUsed : EdgeFunctions ;

	static function main() {
	}
	
	private static function buildGraph( doc : HtmlDom ) : TutorialGraph {
		var graph = new TutorialGraph() ;
		var graphBuilt = true ;
		var graphDomNode : HtmlDom ;
		untyped{ graphDomNode = doc.getElementById("graph") ; }
		if (graphDomNode == null ) {
			trace("No element in the html file has id 'graph'") ;
			return null ; }
		var startFunctionName = graphDomNode.getAttribute("data-function") ;
		if ( startFunctionName != null ) graph.setStartFunctionName( startFunctionName ) ;
		else { trace("Graph node has no 'data-function' attribute") ;
			return null ; }
		var child = graphDomNode.firstChild ;
		while ( child != null ) {
			//trace( "Current child is  " + child ) ;
			//trace( "Node name is " + child.nodeName ) ;
			if ( child.nodeType == NodeTypes.ELEMENT_NODE && child.nodeName == "DIV" ) {
				var klass = child.getAttribute("class")  ;
				// Collect all vertices
				if ( klass == null ) {
					trace("A child of the 'graph' element has no class" ) ;
					graphBuilt = false ;  }
				else if ( klass == "vertex" ) {
					var id : String = child.getAttribute("id") ;
					if ( id == null ) {
						trace("There is a vertex with no id" ) ;
						graphBuilt = false ;}
					else if ( graph.vertices.exists( id ) ) {
						trace( "Duplicate vertex with id '" +id + "'" ) ;
						 graphBuilt = false ; }
					else {
						trace( "Building vertex " + id) ;
						var vertex = new TutorialVertex( id, child ) ;
						graph.vertices.set( id, vertex ) ; 
						if ( graph.startVertex == null ) {
							graph.setStartVertex( vertex ) ; } } }
				// Collect all edges
				else if ( klass == "edge" ) {
					var id : String = child.getAttribute("id") ;
					if ( id == null ) {
						trace("There is an edge with no id" ) ; 
						graphBuilt = false ; } 
					else if ( graph.edges.exists( id ) ) {
						trace( "Duplicate edge with id '" +id + "'" ) ;
						graphBuilt = false ; }
					else {
						trace( "Building edge " + id) ;
						var edge = new TutorialEdge( id, child ) ;
						graph.edges.set( id, edge ) ; } }
				else {
					trace("A child of the 'graph' node has a class '"
						+ klass
						+ "' that is neither 'vertex' nor 'edge'" ) ; } }
			child = child.nextSibling ;  }
			
		// Now link the vertices and edges together
		for ( edge in graph.edges ) {
			
			var edgeLabel = edge.htmlNode.getAttribute("data-label") ;
			if ( edgeLabel == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-label' attribute." ) ;
				graphBuilt = false ; }
			else {
				edge.label = edgeLabel ; }
			
			var functionName = edge.htmlNode.getAttribute("data-function") ;
			if ( functionName == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-function' attribute." ) ;
				graphBuilt = false ; }
			else { 
				edge.functionName = functionName ;
			}
				
			var sourceId = edge.htmlNode.getAttribute("data-source") ;
			if ( sourceId == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-source' attribute." ) ;
				 graphBuilt = false ; }
			else if( ! graph.vertices.exists( sourceId ) ) {
				trace("Edge '" + edge.id + "' has 'data-source' attribute of '" + sourceId + "'. But there is no such node.");
				graphBuilt = false ; }
			else {
				edge.source = graph.vertices.get( sourceId ) ;
				if( edgeLabel != null )
					graph.vertices.get( sourceId ).outGoingEdges.set( edgeLabel, edge ) ;  }
			
			var targetId = edge.htmlNode.getAttribute("data-target") ;
			if ( targetId == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-target' attribute." ) ;
				 graphBuilt = false ; }
			else if( ! graph.vertices.exists( targetId ) ) {
				trace("Edge '" + edge.id + "' has 'data-target' attribute of '" + targetId + "'. But there is no such node.");
				graphBuilt = false ; }
			else {
				edge.target = graph.vertices.get( targetId ) ; }
		}
		if ( graph.startVertex == null ) { graphBuilt = false ; }
		if ( graphBuilt )  return graph ; else return null ;
		
	}
	static function onLoad() {
		trace( "onLoad starts") ;
		var graph = buildGraph( Lib.document ) ;
		if ( graph == null ) { trace("Failed to build graph"); return ; }
		var applet : TMExternalCommandInterface ;
		untyped { applet = Lib.document.applets["tm_applet"]  ;  }
		if ( applet == null ) {
			trace( "Applet not found" ) ;
			return ; }
		trace( "Applet is " + applet ) ;
		tmProxy = new TMProxy( applet ) ;
		executeFunction( graph.startFunctionName ) ;
		switchToVertex( graph.startVertex ) ;
		trace( "onLoad ends" ) ;
	}
	
	static function executeFunction( name : String ) {
		var klass = Type.resolveClass("EdgeFunctions") ;
		if ( klass == null ) {
			trace("No class 'EdgeFunctions' found.") ; }
		else {
			var fun = Reflect.field( klass, name ) ;
			if ( fun == null ) {
				trace( "Function named '" +name + "' not found." ) ; }
			else {
				Reflect.callMethod( klass, fun, [tmProxy] ) ; } }
	}
	
	static function switchToVertex( vertex : TutorialVertex ) {
		
		var instructionNode = Lib.document.getElementById("instructions") ;
		while ( instructionNode.firstChild != null )
			instructionNode.removeChild( instructionNode.firstChild ) ;
		instructionNode.insertBefore( vertex.htmlNode, null ) ;
		
		var buttonsNode = Lib.document.getElementById("buttons") ;
		while ( buttonsNode.firstChild != null )
			buttonsNode.removeChild( buttonsNode.firstChild ) ;
		for ( id in vertex.outGoingEdges.keys()  ) {
			var edge = vertex.outGoingEdges.get( id ) ;
			if (edge.label == "back")
			{
				if (edge.target.id == vertexStack.first().id)
				{
					var target = edge.target ;
					var functionName = edge.functionName ;
					var labelNode = Lib.document.createTextNode(edge.label) ;
					var button = Lib.document.createElement("button") ;
					button.insertBefore( labelNode, null ) ;
					button.onclick = function(event : Event ) {
						executeFunction( functionName ) ;
						vertexStack.pop(); 
						switchToVertex( target ) ; }
					buttonsNode.insertBefore( button, null ) ;
				}
				else
					continue;
			}
			else
			{
				var target = edge.target ;
				var functionName = edge.functionName ;
				var labelNode = Lib.document.createTextNode(edge.label) ;
				var button = Lib.document.createElement("button") ;
				button.insertBefore( labelNode, null ) ;
				button.onclick = function(event : Event ) {
					executeFunction( functionName ) ;
					vertexStack.push(vertex); 
					switchToVertex( target ) ; }
				buttonsNode.insertBefore( button, null ) ; }
		
			}
	}
	
}