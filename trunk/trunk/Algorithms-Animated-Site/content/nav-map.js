// JavaScript Document
// Assumes tocNavigator.js loaded
// Normal starting page should go here
var root = new Node("content/titlePage.html");
var treeWalker = new Walker(root);


// Site Navigation map
// define the directories for convenience
var pages = "content/Pages/";


// Now define the navigation map
root.addChild(new Node(pages + "introduction.html"));
root.addChild(new Node(pages + "Dijkstras-Algorithm/dijkstras.html"));
root.addChild(new Node(pages + "Kruskals-Algorithm/kruskals.html"));
root.addChild(new Node(pages + "PACC/CHAPTER02/segmentsum.html"));
root.addChild(new Node(pages + "PACC/CHAPTER02/dynamicarray.html"));
root.addChild(new Node(pages + "PACC/CHAPTER02/fastintegerproduct.html"));
root.addChild(new Node(pages + "PACC/CHAPTER02/strassen.html"));




