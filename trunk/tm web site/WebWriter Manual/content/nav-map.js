// JavaScript Document
// Assumes tocNavigator.js loaded
// Normal starting page should go here

if( console && console.debug ) console.debug("Start of nav-map.js" );	
var root = new Node("content/titlePage.html");
var treeWalker = new Walker(root);


// Site Navigation map
// define the directories for convenience
var pages = "content/manualPages/";


// Now define the navigation map
root.addChild(new Node(pages + "introduction.htm"));
root.addChild(new Node(pages + "structure.htm"));
root.addChild(new Node(pages + "toolBar.htm"));
root.addChild(new Node(pages + "markup.htm"));
root.addChild(new Node(pages + "creatingNewSite.htm"));
var toolsetNode = new Node(pages + "toolset.htm");
root.addChild(toolsetNode);
toolsetNode.addChild(new Node(pages + "insert_code.htm"));
toolsetNode.addChild(new Node(pages + "hiddenNotes.htm"));
toolsetNode.addChild(new Node(pages + "popups.htm"));
toolsetNode.addChild(new Node(pages + "definitions.htm"));
toolsetNode.addChild(new Node(pages + "rollover_blocks.htm"));
toolsetNode.addChild(new Node(pages + "answerBoxes.htm"));
toolsetNode.addChild(new Node(pages + "symbols.htm"));
toolsetNode.addChild(new Node(pages + "buttons.htm"));
toolsetNode.addChild(new Node(pages + "utilities.htm"));




if( console && console.debug ) console.debug("End of nav-map.js" );	
