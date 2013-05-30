package ;
import js.html.Node ;

/**
 * One node of a tutorial. The vertex coresponds to a div element that
 * contains the description of the TM at this point in the tutorial.
 * @author Theodore Norvell
 */

class TutorialVertex {
	
	public var id(default,null) : String ;
	public var outGoingEdges(default,null) : Map<String,TutorialEdge> ;
	public var htmlNode(default, null) : Node ; 
	public var pageno: Int;
	//public var inComingEdges(default, null) : Hash<TutorialEdge>;

	public function new( id : String, htmlNode : Node, pageno: Int) {
		this.id = id ;
		this.htmlNode = htmlNode ;
		this.outGoingEdges = new  Map<String,TutorialEdge>() ;
		this.pageno = pageno;
		//this.inComingEdges = new Hash<TutorialEdge>() ;
	}
	
}