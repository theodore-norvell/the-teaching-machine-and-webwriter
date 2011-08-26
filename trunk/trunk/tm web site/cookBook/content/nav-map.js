// JavaScript Document
// Assumes tocNavigator.js loaded

var root = new Node("content/titlePage.htm");

// Site Navigation map
// define the directories for convenience
var intro = "content/overview/";
var fundamentals = "content/fundamentals/";
var arrayVisualizations = "content/arrayVisualizations/";


var treeWalker = new Walker(root);

// Now define the navigation map
root.addChild(new Node(intro + "overview.htm"));
root.addChild(new Node(fundamentals + "scripting.htm"));
root.addChild(new Node(fundamentals + "commands.htm"));
root.addChild(new Node(fundamentals + "codeTagging.htm"));
root.addChild(new Node(fundamentals + "style.htm"));
root.addChild(new Node(arrayVisualizations + "elasticArray.htm"));
/* Note that subNodes are allowed as the following (which assumes the definition of variable functions):
var scope = new Node(chapter1 + "scope.htm");
root.addChild(scope);
scope.addChild(new Node(functions + "discussion.htm"));
*/







