// JavaScript Document
/******* Op class ************************************************/
function Op(o,p,a){
	this.operation = o;
	this.precedence = p;
	if (a) this.associativity = a;
}

Op.prototype.toString = function(){
	switch (this.operation){
		case Op.OR: return "+";
		case Op.AND: return "\u2022";
		case Op.NOT: return "~";
		default: return "unrecognized operator";
	}
}

// operations
Op.OR = 1;
Op.AND = 2;
Op.NOT = 3;


// Associativities
Op.LEFT = 1;
Op.RIGHT = 2;



/**** Parser Class *****************************************************************/

// Parser constructor
function Parser(){
	this.selector = "";
}

/* Class attributes *************************/
/* The reference number of the next Button object to be put on this page
 alternatively, the number of Buttons currently on the page
 Note that while button is an object its visble manifestation on the
 page is not the button object but rather an image element.
 Thus each button has an image element which is an object in the DOM tree.
 The button itself is NOT in the DOM tree.
 Thus we keep the buttons separateley in an array (until I can find a way
 to attach them directly to the DOM tree) */
 
Parser.theParser = new Parser();  // Parser is a singleton.
Parser.END = "";
Parser.BinaryOps = new RegExp("[\+\|\.&]");
Parser.UnaryOps = new RegExp("[~\!]");
Parser.Terminators =  new RegExp("[\+\|\.&~\!\(\)]");



/**** Parser instance methods ******************/

// New selection string for parsing
Parser.prototype.setSelection = function(selection){
	Parser.selector = selection;
}

/* Fundamental parsing operations used by most parser algorithms */

Parser.prototype.next = function(){
	var s;
	if(Parser.selector == "") s = Parser.END;
	else {
		var pos = Parser.selector.search(Parser.Terminators);
		if (pos == -1) s = Parser.selector;
		else if (pos == 0) s = Parser.selector.charAt(0);
		else s = Parser.selector.substring(0,pos);
	}
//	alert("next token is " + (s == "" ? "END" : s));
	return s;
}

Parser.prototype.consume = function(){
	if(Parser.selector != "") {
		var pos = Parser.selector.search(Parser.Terminators);
		if (pos == -1) Parser.selector = "";
		else if (pos == 0) Parser.selector = Parser.selector.substring(1);
			else Parser.selector = Parser.selector.substring(pos);
	}
//	alert ("selector after consume is " + Parser.selector);
}

Parser.prototype.error = function(message){
//	alert ("parser error: " + message);
}

Parser.prototype.expect = function(token){
//	alert("expect(" + token + ")");
	if( this.next() == token)
		this.consume();
	else this.error("was expecting " + (token == "" ? "END" : token));
}

Parser.prototype.isBinary = function(token){
	return token.search(Parser.BinaryOps) == 0;
}

Parser.prototype.isUnary = function(token){
	return token.search(Parser.UnaryOps) == 0;
}

Parser.prototype.isValue = function(token){
	if(this.isUnary(token)) return false;
	if(this.isBinary(token)) return false;
	if (token == Parser.END) return false;
	if (token == "(" || token == ")") return false;
	return true;
}


Parser.prototype.binary = function(token){
	var binaryOp = null;
	if (!this.isBinary(token)) this.error(token + "is not a binary op.");
	else {
		switch(token){
			case "&":
			case ".":
				binaryOp =  new Op(Op.AND, 2, Op.LEFT);
				break;
			
			case "|":
			case "+":
				binaryOp =  new Op(Op.OR, 1, Op.LEFT);
				break;
			default: this.error("undefined binary op.");
		}
	}
//	alert("Created BinaryOp " + Op.toString());
	return binaryOp;
}

Parser.prototype.unary = function(token){
	if (!this.isUnary(token)) this.error(token + "is not a unary op.");
	else {
		if (token == '!' || token == '~')
			return new Op(Op.NOT,3, null);
		else this.error("undefined unary op.");
	}
}

			


/* Recursive Descent Parser */

Parser.prototype.eParser = function(){
	var t = this.Exp(0);
	this.expect(Parser.END);
	return t;
}

Parser.prototype.Exp = function(p){
//	alert("Exp("+p+")");
	var t = this.Primary();
	var token = this.next();
	while (this.isBinary(token) && this.binary(token).precedence >= p) {
		var op = this.binary(token);
		this.consume();
		var t1 = this.Exp((op.associativity == Op.LEFT ? op.precedence+1 : op.precedence));
		t = Tree.makeNode(op, t, t1);
		token = this.next();
	}
//	alert("Exp(" + p + ") returning " + t);
	return t;
}

Parser.prototype.Primary = function(){
	var next = this.next();
	var tree;
	if (this.isUnary(next)){
		var op = this.unary(next);
		this.consume();
		var t = this.Exp(op.precedence);
		tree = Tree.makeNode(op,t);
	} else if (next == "(") {
		this.consume();
		tree = this.Exp(0);
		this.expect(")");
	} else if (this.isValue(next)){
		tree = Tree.makeLeaf(next);
		this.consume();
	} else {
		this.error("Primary: token " + next + " is not a unary, a ( or a ).");
		tree = null;
	}
//	alert("Primary returning " + tree);
	return tree;
}



// Tree Class **********************************************
function Tree(op,left, right,token){
	if (op != null & token & token != null) alert("Tree constructor: can't define both token and operation!"); 
	this.op = op;
	this.left = left;
	this.right = right;
	this.token = token;
}

// Class factory functions
Tree.makeLeaf = function(token){
//	alert("makeLeaf("+token+")");
	if (token == "SCRIPT") token = "S";
	else if (token == "LIB") token = "L";
	return new Tree(null, null, null, token);
}

Tree.makeNode = function(op, left, right){
	if(!right) right = null;
//	alert("makeNode("+op+", " + left + ", " + right + ")");
	return new Tree(op, left, right, null);
}

Tree.prototype.evaluate = function(set){
	if (this.op == null) return set.contains(this.token);
	else {
		switch (this.op.operation) {
			case Op.OR: 
				return  this.left.evaluate(set) || this.right.evaluate(set);
			case Op.AND: 
				return  this.left.evaluate(set) && this.right.evaluate(set);
			case Op.NOT: 
				return  !this.left.evaluate(set);
			default:
				alert("evaluate doesn't recognize op " + this.op);
		}
	}
}

Tree.prototype.toString = function(){
	var s;
	if (this.op == null) 
		s = this.token;
	else {
		s = this.left + " ";
		if (this.right != null) s+= this.right + " ";
		s+= this.op + " ";
	}
	return s;
}

Tree.prototype.dump = function(){
	document.write("Tree in RPN: " + this.toString());
}

/*** Set Class **************************/

function Set(){
	this.mySet = new Array();
}

Set.prototype.contains = function(item){
	for(var i = 0; i < this.mySet.length; i++)
		if (this.mySet[i] == item) return true;
	return false;
}

Set.prototype.add = function(item){
	if (this.contains(item)) this.error(item + " already in Set");
	this.mySet.push(item);
//	alert ("after adding " + item + " set length is " + this.mySet.length);
}

Set.prototype.remove = function(item){
	for(var i = 0; i < this.mySet.length; i++)
		if (this.mySet[i] == item) {
			this.mySet[i] = this.mySet[this.mySet.length-1];
			this.mySet.pop();
			break;
		}
}

Set.prototype.toString = function(){
	var s = "Set{";
	for (var i = 0; i < this.mySet.length - 1; i++)
		s += this.mySet[i] + ", ";
	if (this.mySet.length > 0) s += this.mySet[this.mySet.length-1];
	s += "}";
	return s;
}
	
	


	








