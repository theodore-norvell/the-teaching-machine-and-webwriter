// utilities.js -- Generally useful functions.


function dumpDocument(node) {
	try {
		//alert( "dumping all") ;
		var string = format(node, 0, "") ;
		putToJSConsole( string ) ; }
    catch( ex ) {
        unexpectedException( "dumpAll", ex ) ; }
}


function format( node, indent, string ) {
	var i ; for( i=0 ; i < indent ; ++i ) string += "   " ;
	if( node.nodeType == node.ELEMENT_NODE ) {
		string += "<" + node.tagName + ">\n" ;
	}
	else if( node.nodeType == node.TEXT_NODE ) {
		string += "'" + node.data + "'\n" ;
	}
	else if( node.nodeType == node.CDATA_SECTION_NODE ) {
		string += "CDATA: '" + node.data + "'\n" ;
	}
	else if( 1 <= node.nodeType && node.nodeType <= 12 ) {
		string += 'nodeType is ' + format.nodeNames[node.nodeType]
		        + ' nodeName is ' + node.nodeName + "\n" ;
	}
	else {
		string += 'unknown nodeType: ' + node.nodeType + '\n' ;
	}
	var child = node.firstChild ;
	while( child != null ) {
		string = format( child, indent+1, string ) ;
		child = child.nextSibling ;	}
	
	return string ;
}

format.nodeNames = ["", "ELEMENT_NODE", "ATTRIBUTE_NODE", "TEXT_NODE",
                    "CDATA_SECTION_NODE", "ENTITY_REFERENCE_NODE", "ENTITY_NODE",
                    "PROCESSING_INSTRUCTION_NODE", "COMMENT_NODE", "DOCUMENT_NODE",
                    "DOCUMENT_TYPE_NODE", "DOCUMENT_FRAGMENT_NODE", "NOTATION_NODE" ] ;

function formatObject( object ) {
	var string = "object:\n" ;
	for( var prop in object ) {
		string += "  "+prop+": "+object[prop] + "\n" ;
	}
	return string ;
}

function putToJSConsole(str)
{
  Components.classes['@mozilla.org/consoleservice;1']
            .getService(Components.interfaces.nsIConsoleService)
            .logStringMessage(str);
}


function putToConsole(str)
{
  dump(str) ;
}


function $(idStr) {
	var  elem = document.getElementById(idStr);
	assertCheck( elem != null, "No such element "+idStr) ;
	return elem ;
}

function nodeOffset(node) {
	assertCheck( node.parentNode ) ;
	var sibList = node.parentNode.childNodes ;
	var len = sibList.length ;
	for( var i = 0 ; i < len ; ++i ) {
		if( sibList.item(i) == node ) return i ; }
	assertNotReached() ;
}

/** Recursively create a text node or an element in the current document.
 * Based on code by Stephen Sorensen.  Thanks.
 * 
 * If arg is a string, it creates a new text node and returns that.
 * If arg is a dom node, it returns arg
 * If arg is an object with a tag property, it creates a new element, best explained by an
 * example:
 * <pre>
 *    var node = createNode({  
 *            tag: "a",  
 *            attributes: {  
 *               href: "http://www.google.com"  
 *            },  
 *            style: {  
 *                textDecoration: "none"  
 *            },  
 *            children: [ "Click Here!" ]  // And this is recursive!
 *    });
 *   document.body.appendChild(node);
 * </pre>
 *   http://spudly.shuoink.com/2008/02/20/using-the-xml-dom-without-writing-150000-lines-of-code/
 *   
 * @param arg is either a string or an object.
 * @return the newly created node.
 * 
 */  
function createNode( arg ) {  
	var node;  
    if( typeof(arg) == "string" ) {  
   
       node = document.createTextNode(arg);  }
       
    else if ( typeof(arg) == "object" ) {  
       
    	if( arg.nodeType ) {
    		
    		// Assume it is a DOM node already
    		return arg ; }
    		
    	else {
    		
			assertCheck( arg.tag,
				"createNode  requires objects with a 'tag' property." ) ;
   
			node = document.createElement( arg.tag );  
   
			if ( arg.attributes ) {  
				for ( i in arg.attributes ) {  
					node.setAttribute(i, arg.attributes[i]); } }  
             
			if ( arg.style ) {  
				for ( i in arg.style ) {  
					node.style[i] = arg.style[i]; } }
             
			if ( arg.children ) {  
				for ( var i = 0; i < arg.children.length; i++ ) {  
				node.appendChild( createNode( arg.children[i] ) ); } } } }
             
	else
		assertNotReached("createNode argument type is wrong") ;
	return node;  
}


/** This function is needed by Venkman.
 * See http://developer.mozilla.org/en/docs/Debugging_a_XULRunner_Application
 * for more information.
 */
function toOpenWindowByType(inType, uri) {
  var winopts = "chrome,extrachrome,menubar,resizable,scrollbars,status,toolbar";
  window.open(uri, "_blank", winopts);
}