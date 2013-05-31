// JavaScript Document
/** tocNavigator.js *******************************************************
	ASSUMES: constants.js already loaded
	LOCATED: in wwFolder
	DESCRIPTION:
		Provides routines for walking a site navigation tree which is
		defined by a separate, site-specific nav-map.js
***************************************************************************/
//if( console && console.debug ) console.debug("Start of tocNavigator.js" );	

/******** Node Class ******************************/

function Node(doc){
	this.parent = null;
	this.previous = null;
	this.next = null;
	this.firstChild = null;
	this.doc = doc;
	this.level = 0;
}

Node.prototype.toString = function(){
	return this.doc;
}

Node.prototype.dumpNode = function(){
	var dump = "";
	dump += "parent: " + (this.parent == null ? "null" : this.parent.doc) + "\n";
	dump += "previous: " + (this.previous == null ? "null" : this.previous.doc) + "\n";
	dump += "next: " + (this.next == null ? "null" : this.next.doc) + "\n";
	dump += "firstChild: " + (this.firstChild == null ? "null" : this.firstChild.doc) + "\n";
	return dump;
}
	
Node.prototype.addChild = function(childNode){
	if (this.firstChild == null) this.firstChild = childNode;
	else {
		var lastChild = this.firstChild;
		var secondLast = null;
		while (lastChild!=null) {
			secondLast = lastChild;
			lastChild = lastChild.next;
		}
		lastChild = childNode;
		if (secondLast != null) {
			secondLast.next = lastChild;
			lastChild.previous = secondLast;
		}
	}
	childNode.parent = this;
	childNode.level = this.level + 1;
/*	alert("added " + childNode.doc + "\nprevious = " + (childNode.previous ? childNode.previous.doc : "null") +
		  	"\nnext = " + (childNode.next ? childNode.next.doc : "null") + 
			"\nparent = " + (childNode.parent ? childNode.parent.doc : "null"));*/
}
/******* end of Node Class *****************************/


/******* Walker Class *****************************/
// Don't use TreeWalker as it is a built-in class

function Walker(startNode){
	this.current = startNode;
	this.root = startNode;
}

Walker.prototype.toRoot = function(){
	this.current = this.root;
}

Walker.prototype.set = function(node){
	this.current = node;
}

// flat move - next sibling
Walker.prototype.next = function(){
	if (this.current.next != null)
		this.current = this.current.next;
}		

// flat move - previous sibling
Walker.prototype.previous = function(){
	if (this.current.previous != null)
		this.current = this.current.previous;
}		

Walker.prototype.up = function(){
	if (this.current.parent != null)
		this.current = this.current.parent;
}

Walker.prototype.down = function(){
	if (this.current.firstChild != null)
		this.current = this.current.firstChild;
}
// hierarchial move - into children first can end up at a null - walks whole tree
Walker.prototype.walk = function(){
	if (this.current.firstChild != null)
		this.current = this.current.firstChild;
	else if (this.current.next != null)
			this.current = this.current.next;
	else {
		this.current = this.current.parent;
		if (this.current != null)
			this.current = this.current.next;
	}
//	alert("Walker at " + (this.current == null ? "null" : this.current.doc));
}

Walker.prototype.dump = function(){
	var theTree = "";
	var current = this.current;
	this.toRoot();
	while (this.current != null){
		theTree += "  " + this.current.toString();
		this.walk();
	}
	return theTree;
}
		
/******** End of Walker Class ***********************/
//if( console && console.debug ) console.debug("End of tocNavigator.js" );	

