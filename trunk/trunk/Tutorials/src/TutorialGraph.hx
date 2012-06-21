package ;

/**
 * ...
 * @author Theodore Norvell
 */

class TutorialGraph {
	public var vertices(default, null) : Hash<TutorialVertex>;
	public var edges(default, null) : Hash<TutorialEdge> ;
	public var startVertex( default, null) : TutorialVertex ;
	public var startFunctionName : String ;

	public function new() {
		this.vertices = new Hash<TutorialVertex>()  ;
		this.edges = new Hash<TutorialEdge>()  ;
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