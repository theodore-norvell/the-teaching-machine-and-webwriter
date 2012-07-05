package ;
import js.Dom ;

/**
 * One node of a tutorial. The vertex coresponds to a div element that
 * contains the description of the TM at this point in the tutorial.
 * @author Theodore Norvell
 */

class TutorialVertex {
	
	public var id(default,null) : String ;
	public var outGoingEdges(default,null) : Hash<TutorialEdge> ;
	public var htmlNode(default, null) : HtmlDom ; 
	//public var inComingEdges(default, null) : Hash<TutorialEdge>;

	public function new( id : String, htmlNode : HtmlDom) {
		this.id = id ;
		this.htmlNode = htmlNode ;
		this.outGoingEdges = new Hash<TutorialEdge>() ;
		//this.inComingEdges = new Hash<TutorialEdge>() ;
	}
	
}