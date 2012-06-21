package ;

/**
 * ...
 * @author Theodore Norvell
 */

 import js.Dom ;

class TutorialEdge {
	
	public var id(default,null) :String ;
	public var htmlNode(default,null) : HtmlDom ;
	public var source : TutorialVertex ;
	public var target : TutorialVertex ;
	public var functionName : String ;
	public var label : String ; // Used for the name of the button.

	public function new( id : String, htmlNode : HtmlDom ) {
		this.id = id ;
		this.htmlNode = htmlNode ;
	}
	
}