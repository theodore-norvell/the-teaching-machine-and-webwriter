// JavaScript Document
/*** Set Class **************************
* A standard set which allows only one of 
* each item. Duplicates are defined by the ==
* operation

****************************************/


/*  Constructor ****************
* @action: creates an empty Set object
****************************************/
function Set(){
	this.mySet = new Array();
}

/* contains ***************************************
* @param: item - item being searched for in the set
* @returns: true if item is in the set.
****************************************************/
Set.prototype.contains = function(item){
	with (this) {
		for(var i = 0; i < mySet.length; i++)
			if (mySet[i] == item)
				return true;
	}
	return false;
}

/* getSize ***************************************
* @returns: no. of items in the set
****************************************************/
Set.prototype.getSize = function(){
	return this.mySet.length;
}

/* contains ***************************************
* @param: i - location of item in the set
* @returns: true if item is in the set.
Set.prototype.getItem = function(i){
	with (this){
		if (i >=0 && i < mySet.length)
			return mySet[i];
		else return null;
	}
}
****************************************************/

/* add ***************************************************************
* @param: item - item being added to the set
*            @pre: item is not already in the set
* @action: adds item to the set
* @post: set size increases by 1
************************************************************************/
Set.prototype.add = function(item){
	with (this) {
		if (!contains(item)) 
			mySet.push(item);
//	alert ("after adding " + item + " set length is " + this.mySet.length);
	}
}

/* remove ***************************************************************
* @param: item - item to be removed from the set
*          @ pre: item is in the set
* @action: removes item from the set 
* @post: set size decreases by 1
************************************************************************/
Set.prototype.remove = function(item){
	with (this) {
		for(var i = 0; i < mySet.length; i++)
			if (mySet[i] == item) {
				mySet[i] = mySet[mySet.length-1];
				mySet.pop();
				break;
			}
	}
}

/* clear ***************************************************************
* @action: removes all items from the set 
* @post: set size = 0
************************************************************************/
Set.prototype.clear = function(){
	this.mySet.length = 0;	// According to Flanagan (section 7.6.5) this kills data
}


/* toString ***************************************************************
* @action: over-ride of standard object.toString function 
************************************************************************/
Set.prototype.toString = function(){
	var s = "Set{";
	with (this) {
		for (var i = 0; i < mySet.length - 1; i++)
			s += mySet[i] + ", ";
		if (mySet.length > 0) s += mySet[mySet.length-1];
		s += "}";
	}
	return s;
}

/*****************************************************************
TokenSet class is a SubClass of Set to represent a Set of Tokens
******************************************************************/
function TokenSet(){
	Set.call(this); // just call set constructor
}

/* TokenSet prototype subclasses object prototype unless we explicitly
    create prototype object as a subClass of Set object */
TokenSet.prototype = new Set();

/* TokenSet constructor is now Set constructor unless it gets set
   explicitly. This is probably all overkill in that no properties
   are being added by TokenSet, only one new method.
*/
TokenSet.prototype.constructor = TokenSet;


/* getPosIn ***************************************************************
* @param: str - a string to be searched
*         start - (optional) position in string to start search
*                  defaults to 0
* @action: searches string for the first occurence of any item in the set in the string
* @comment: this is not a generic set function but is explicitly to support the
*            the use of sets in a parser. It assumes that all items in the set
*             are strings.
* @returns: position of first occurence of any item in the set in the string
*             or -1 if no items are found
************************************************************************/

TokenSet.prototype.getPosIn = function(str, start){
	var pos = -1;
	with (this){
		if (!start) start = 0;
		for (var i = 0; i < mySet.length; i++){
			var p = str.indexOf(mySet[i], start);
			if (p > -1){
				if (pos == -1) pos = p;
				else if (pos > p) pos = p;
			}
		}
	}
	return pos;
}







/******* Op class ************************************************/


/*  Constructor ****************
* @param: symbol - the token string used to represent the operator
*         parseSymbol - string used to output symbol in toString
*         precedence - an int representing the operator precedence
*         opFunction - the function used to evaluate the operator
*         associativity - Op.UNARY for unary ops, Op.LEFT or Op.RIGHT for binary
* @action: create an object to represent a symbol
****************************************/

function Op(symbol, parseSymbol, precedence, opFunction, associativity){
	this.symbol = symbol;
	this.parseSymbol = parseSymbol;
	this.precedence = precedence;
	this.opFunction = opFunction;
	this.associativity = associativity;
}



// Associativities
Op.UNARY = 0;
Op.LEFT = 1;
Op.RIGHT = 2;

/* isUnary ***************************************************************
* @returns: true only if the operator is unary 
************************************************************************/
Op.prototype.isUnary = function(){
	return this.associativity == Op.UNARY;
}

/* isBinary ***************************************************************
* @returns: true only if the operator is binary 
************************************************************************/
Op.prototype.isBinary = function(){
	return this.associativity != Op.UNARY;
}


/* toString ***************************************************************
* @action: over-ride of standard object.toString function 
************************************************************************/
Op.prototype.toString = function(){
	with(this) {
		return "{" + symbol + ", " + parseSymbol + ", " + precedence + ", " + opFunction + ", " + associativity + "}\n";
	}
}

/******* OpTable class ************************************************/



/*  Constructor *************************************
* @param: aTable (optional) - an array of Op objects 
* @action: creates an operator table for use with a
*          recursive descent parser
****************************************************/
function OpTable(aTable){
	this.binaryOps = new TokenSet(); // set of binary operator symbols
	this.unaryOps = new TokenSet();	// set of unary operator symbols
	this.terminators =  new TokenSet();  // set of terminator symbols
	this.terminators.add("(");
	this.terminators.add(")");
	this.tableOfOps = (aTable ? aTable : new Array()); // Actual table of all operators
	with (this) {
		for(var i = 0; i < tableOfOps.length; i++)
			putInSets(tableOfOps[i]);
	}
}


/*  add *************************************
* @param: op - an Op object 
* @action: adds the Op object to the OpTable
****************************************************/
OpTable.prototype.add = function(op){
	with (this) {
		tableOfOps.push(op);
		putInSets(op);
	}
}

/*  putInSets *************************************
* @param: op - an Op object 
* @comment: a utility function to share code common to
*           the constructor and the add function. Should
*            not be called independently (no private
*            functions in js)
* @action: adds Op.symbol to appropriate set(s)
****************************************************/
OpTable.prototype.putInSets = function(op){
	with (this) {
		if (op.isUnary() && !unaryOps.contains(op.symbol))
			unaryOps.add(op.symbol);
		else if (!binaryOps.contains(op.symbol))
			binaryOps.add(op.symbol);
		if (!terminators.contains(op.symbol))
			terminators.add(op.symbol);
	}
}


/*  findUnary ****************************************************
* @param: opSymbol - an Op symbol 
*            functions in js)
* @action: searches table for a unary Op object having that symbol
* @ returns: Op if found or null if no unary Op with that symbol
*******************************************************************/
OpTable.prototype.findUnary = function(opSymbol){
	with(this){
		for(var i = 0; i < tableOfOps.length; i++){
			var current = tableOfOps[i]
			if (current.symbol == opSymbol  && current.isUnary())
				return current;
		}
	}
	return null;
}

/*  findBinary ****************************************************
* @param: opSymbol - an Op symbol 
*            functions in js)
* @action: searches table for a binary Op object having that symbol
* @ returns: Op if found or null if no binary Op with that symbol
*******************************************************************/
OpTable.prototype.findBinary = function(opSymbol){
	with(this){
		for(var i = 0; i < tableOfOps.length; i++){
			var current = tableOfOps[i]
			if (current.symbol == opSymbol  && current.isBinary())
				return current;
		}
	}
	return null;
}

/* toString ***************************************************************
* @action: over-ride of standard object.toString function 
************************************************************************/
OpTable.prototype.toString = function(){
	var s = "OpTable" ;
	with (this){
		for(var i = 0; i < tableOfOps.length; i++){
			s += tableOfOps[i].toString();
		}
	}
	return s;
}


function Alias(alias, primary){
	this.alias = alias;
	this.primary = primary;
}


/* Tree Class ************************************************
* a class to represent a tree as required by a parser. That is
* it comes equipped with a recursive evaluate method
**************************************************************/


/*  Constructor **********************************************
* @param: op - a valid Op object
*         left - a Tree corresponding to a left argument
*         right - a Tree corresponding to a right argument
*         v - a terminal corresponding to a variable or value
*      @pre: EITHER op and left are valid objects and v is null OR
*                   v is a valid object and op, left and right are all null
* @action: creates an operator table for use with a
*          recursive descent parser
****************************************************/

function Tree(op, left, right, v){
	if (op != null & v & v != null) alert("Tree constructor: can't define both terminal and operation!"); 
	this.op = op;
	this.left = left;
	this.right = right;
	this.terminal = v;
}

// Class variables
Tree.leafEquivalents = null;  // optional array of terminal Alias objects
Tree.evaluateLeaf;			// the function to evaluate leaf terminals

// Class factory functions

/*  setLeafEval ***************************************************
* @param: evalLeafFunction - a valid function for evaluating leaf
*                 terminals of the form evaluate(terminal, context)
* @comment: The operation of the function is entirely up to the parser's
*            client as is the context. These are implemented entirely by the
*            client. The basic requirement is for the function to return
*            a valid value for any terminal where a valid value is one
*            which can be processed by the Op functions in the OpTable.
* @action: sets the leaf evaluation function for the Tree Class
****************************************************/
Tree.setLeafEval = function(evalLeafFunction){
	Tree.evaluateLeaf = evalLeafFunction;
}
	

/*  setEquivs *********************************************************
* @param: leafEquivalents - an array of terminal Alias objects
*                @pre: leafEquivalents[i] should refer to a valid Alias
*                     object for i on [0, length-1]
* @comment: This facility was included to allow verbose and short names
*            for some critical variables
* @action: sets the leafEquivalents variable for the Tree Class
***********************************************************************/
Tree.setEquivs = function(leafEquivalents) {
	Tree.leafEquivalents = leafEquivalents;
}

/*  makeLeaf **********************************************************
* @param: terminal - the terminal id or literal to be used in the leaf
*                @pre: must be evaluable by the user provided
*                      Tree.evaluateLeaf function
*                     object for i on [0, length-1]
* @action: leaf factory method. Encapsulates the terminal in Tree
*           containing a single leaf. If the terminal is found in
*            the leafEquivalents table the primary terminal is substituted.
* @returns: a Tree object consisting of a single leaf encapsulating
*             the terminal
**************************************************************************/
Tree.makeLeaf = function(terminal){
//	alert("makeLeaf("+token+")");
	if (Tree.leafEquivalents != null)
		for (var i = 0; i < Tree.leafEquivalents.length; i++) {
			if (terminal == Tree.leafEquivalents[i].alias) {
				terminal = Tree.leafEquivalents[i].primary;
				break;
			}
		}
	return new Tree(null, null, null, terminal);
}

/*  makeNode **********************************************************
* @param: op - a valid Op object
*         left - a Tree corresponding to a left argument
*         right - (optional) a Tree corresponding to a right argument
* @action: node factory method. 
* @returns: a Tree object consisting of a node with an Op and a left Tree
*             (and possibly a right Tree) 
**************************************************************************/
Tree.makeNode = function(op, left, right){
	if(!right) right = null;
//	alert("makeNode("+op+", " + left + ", " + right + ")");
	return new Tree(op, left, right, null);
}

/*  evaluate **********************************************************
* @param: context - a user provided context 
* @action: recursive evaluation for applying evaluation of an expression
*           (as represented by the Tree object). When nodes are encountered
*            the left and right trees are called recursively and then the
*            operator function referenced by the Op object is called.
*                  The recursion bottoms at leaf objects by passing the
*            context argument to the user defined evaluateLeaf function,
*            the only time the context is used. 
* @returns: whatever result the user defined Op and evaluateLeaf functions
*            combine to produce.
**************************************************************************/
Tree.prototype.evaluate = function(context){
	with (this){
		if (op == null) return Tree.evaluateLeaf(terminal, context); // Need a general assignment of value
		if (op.isUnary())
			return op.opFunction(left.evaluate(context));
		if (op.isBinary())
			return op.opFunction(left.evaluate(context), right.evaluate(context));
		alert("evaluate doesn't recognize op " + this.op);
	}
}
		

/* toString ***************************************************************
* @action: over-ride of standard object.toString function. In this case
*    generates an RPN representation of the Parse tree
************************************************************************/
Tree.prototype.toString = function(){
	var s;
	with (this){
		if (op == null) 
			s = terminal;
		else {
			s = left + " ";
			if (right != null) s+= right + " ";
			s+= op.parseSymbol + " ";
		}
	}
	return s;
}

Tree.prototype.dump = function(){
	document.write("Tree in RPN: " + this.toString());
}




/**** Parser Class *****************************************************************
* A recursive descent parser based on the Theo Norvell's article "Parsing Expressions
by Recursive Descent" ( http://www.engr.mun.ca/~theo/Misc/exp_parsing.htm )
**************************************************************************************/

/*  Constructor ********************************************
* @param: opTable - an OpTable object 
*           @pre: opTable[i] refers to a valid Op object for i on [0, length-1]
*         evalLeaf - a function of the form evaluateLeaf(terminal, context) which is used as
*                     the Tree.evaluateLeaf function in the Tree class (see the class)
*           @pre: function must be able to evaluate all leaf terminals for all contexts - how
*                  that is done is entirely up to the user
*         leafEquivs: an (optional) Array of Alias objects defining aliases for leaf names
* @action: creates a recursive descent parser driven by the 
*           Op definitions contained in the OpTable object
************************************************************/
function Parser(opTable, evalLeaf, leafEquivs){
	this.opTable = opTable;
	this.evalLeafFunction = evalLeaf;
	this.leafEquivalents = leafEquivs;
	this.theParseString = "";
}

/* Class attributes *************************/
 
Parser.END = "";

/**** Parser instance methods ******************/

/*  setParseString ********************************************
* @param: parseString - a string to be parsed 
* @action: sets the internal parseString
************************************************************/
Parser.prototype.setParseString = function(parseString){
	this.theParseString = parseString;
}

/* Fundamental parsing operations used by most parser algorithms */

/*  next *****************************************************
* @modifies: nothing.
* @returns: the first token from theParseString 
*************************************************************/

Parser.prototype.next = function(){
//	alert ("next: theParseString = " + this.theParseString);
	var s;
	with (this){
		if(theParseString == "") s = Parser.END;
		else {
			var pos = opTable.terminators.getPosIn(theParseString);
			if (pos == -1) s = theParseString;
			else if (pos == 0) s = theParseString.charAt(0);
			else s = theParseString.substring(0,pos);
		}
	//	alert("next token is " + (s == "" ? "END" : s));
	}
	return s;
}

/*  consume *****************************************************
* @modifies: the first token in theParseString is removed
*************************************************************/
Parser.prototype.consume = function(){
	with (this){
		if(theParseString != "") {
			var pos = opTable.terminators.getPosIn(theParseString);
			if (pos == -1) theParseString = "";
			else if (pos == 0) theParseString = theParseString.substring(1);
				else theParseString = theParseString.substring(pos);
		}
	//	alert ("theParseString after consume is " + Parser.theParseString);
	}
}

/*  error *****************************************************
* @param: message - a string defining desired message
* @action: message is displayed using the alert mechanism
*************************************************************/
Parser.prototype.error = function(message){
	alert ("parser error: " + message);
}

/*  expect *****************************************************
* @param: token - a string defining the desired token
* @action: consumes token if it is found otherwise calls error
* @modifies: token is removed from the front of theParseString
*************************************************************/
Parser.prototype.expect = function(token){
	with (this){
	//	alert("expect(" + token + ")");
		if( next() == token)
			consume();
		else error("was expecting " + (token == "" ? "END" : token));
	}
}

/*  isBinary *****************************************************
* @param: token - a string defining the operator token
* @returns: true iff token is a binary Op
*************************************************************/
Parser.prototype.isBinary = function(token){
	return this.opTable.binaryOps.getPosIn(token) == 0;
}

/*  isUnary *****************************************************
* @param: token - a string defining the operator token
* @returns: true iff token is a unary Op
*************************************************************/
Parser.prototype.isUnary = function(token){
	return this.opTable.unaryOps.getPosIn(token) == 0;
}

/*  isValue *****************************************************
* @param: token - a string defining the operator token
* @returns: true iff token represents a value
*************************************************************/
Parser.prototype.isValue = function(token){
	with(this){
		if(isUnary(token)) return false;
		if(isBinary(token)) return false;
		if (token == Parser.END) return false;
		if (token == "(" || token == ")") return false;
	}
	return true;
}


/*  binary *****************************************************
* @param: token - a string representing a binary operator
* @action: searches for binary Op object and calls error if not found
* @returns: The equivalent Op object or null if none 
*************************************************************/
Parser.prototype.binary = function(token){
	with(this){
		var binaryOp = opTable.findBinary(token);
		if (binaryOp == null) error(token + "not a binary op");
//	alert("Created BinaryOp " + Op.toString());
		return binaryOp;
	}
}

/*  unary *****************************************************
* @param: token - a string representing a unary operator
* @action: searches for unary Op object and calls error if not found
* @returns: The equivalent Op object or null if none 
*************************************************************/
Parser.prototype.unary = function(token){
	with(this){
		var unaryOp = opTable.findUnary(token);
		if (unaryOp == null) error(token + "not a unary op");
//	alert("Created unaryOp " + Op.toString());
		return unaryOp;
	}
}

			


/* The following methods implement a Recursive Descent Parser specifically */

/*  eParser *****************************************************
* @action: start parsing theParseString
* @returns: Tree object representing fully parsed string 
*************************************************************/
Parser.prototype.eParser = function(){
	with (this) {
		Tree.setEquivs(leafEquivalents);
		Tree.setLeafEval(evalLeafFunction);
		var t = Exp(0);
		expect(Parser.END);
		return t;
	}
}

/*  Exp *****************************************************
* @param: p - integer representing precedence
BAD DEFINITION!!!!
* @action: builds tree for an expression of precedence p
* @returns: SubTree object representing expression 
*************************************************************/
Parser.prototype.Exp = function(p){
//	alert("Exp("+p+")");
	with (this) {
		var t = Primary();
		var token = next();
		while (isBinary(token) && binary(token).precedence >= p) {
			var op = binary(token);
			consume();
			var t1 = Exp((op.associativity == Op.LEFT ? op.precedence+1 : op.precedence));
			t = Tree.makeNode(op, t, t1);
			token = next();
		}
	//	alert("Exp(" + p + ") returning " + t);
	return t;
	}
}

Parser.prototype.Primary = function(){
	with (this) {
		var theNext = next();
		var tree;
		if (isUnary(theNext)){
			var op = unary(theNext);
			consume();
			var t = Exp(op.precedence);
			tree = Tree.makeNode(op,t);
		} else if (theNext == "(") {
			consume();
			tree = Exp(0);
			expect(")");
		} else if (isValue(theNext)){
			tree = Tree.makeLeaf(theNext);
			consume();
		} else {
			error("Primary: token " + theNext + " is not a unary, a ( or a ).");
			tree = null;
		}
	//	alert("Primary returning " + tree);
		return tree;
	}
}



	
	

	








