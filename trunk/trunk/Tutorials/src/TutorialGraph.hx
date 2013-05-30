package ;

/**
 * ...
 * @author Theodore Norvell
 */

class TutorialGraph {
	public var vertices(default, null) : Map<String,TutorialVertex>;
	public var edges(default, null) : Map<String,TutorialEdge> ;
	public var startVertex( default, null) : TutorialVertex ;
	public var startFunctionName : String ;

	public function new() {
		this.vertices = new Map<String,TutorialVertex>()  ;
		this.edges = new Map<String,TutorialEdge>()  ;
		startVertex = null ;
		startFunctionName = null ;
	}
	
	public function setStartVertex( n : TutorialVertex ) {
		startVertex = n ;
		// trace("Setting startVertex to " + startVertex) ;
	}
	
	public function setStartFunctionName( name : String ) {
		startFunctionName = name ;
	}
}