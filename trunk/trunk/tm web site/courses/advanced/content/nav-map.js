// JavaScript Document
// Assumes tocNavigator.js loaded
// Normal starting page should go here
var root = new Node("content/titlePage.htm");
var treeWalker = new Walker(root);
var siteTitle = "P2 Reference Notes";


// Site Navigation map
// define the directories for convenience
var review = "content/review/";
var pointers = "content/pointers/";
var compound = "content/compound/";
var classes = "content/classes/";
var constructors = "content/constructors/";
var inheritance = "content/inheritance/";
var polymorphism = "content/polymorphism/";



// Now define the navigation map
root.addChild(new Node(review + "variables.htm"));
root.addChild(new Node(review + "constants.htm"));
root.addChild(new Node(review + "functions.htm"));
root.addChild(new Node(review + "string.htm"));
root.addChild(new Node(review + "arrays.htm"));
root.addChild(new Node(pointers + "pointers.htm"));
root.addChild(new Node(pointers + "morePointers.htm"));
root.addChild(new Node(pointers + "allocation.htm"));
root.addChild(new Node(compound + "structures.htm"));
root.addChild(new Node(classes + "introduction.htm"));
root.addChild(new Node(classes + "theUML.htm"));
root.addChild(new Node(classes + "classes.htm"));
root.addChild(new Node(classes + "runningExamples.htm"));
root.addChild(new Node(constructors + "constructors.htm"));
root.addChild(new Node(constructors + "copycons.htm"));
root.addChild(new Node(inheritance + "inheritance.htm"));
root.addChild(new Node(polymorphism + "overloading.htm"));
root.addChild(new Node(polymorphism + "parametric.htm"));

