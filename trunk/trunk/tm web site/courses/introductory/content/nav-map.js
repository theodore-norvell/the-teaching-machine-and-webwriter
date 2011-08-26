// JavaScript Document
// Assumes tocNavigator.js loaded

var root = new Node("content/titlePage.htm");

// Site Navigation map
// define the directories for convenience
var intro = "content/introduction/";
var variables = "content/variables/";
var functions = "content/functions/";
var strings = "content/strings/";
var controls = "content/control_flow/";
var arrays = "content/arrays/";
var graphics = "content/graphics/";
var oddNsods = "content/oddNsods/";
var streams = "content/streams/";
var notes = "content/issues/";
var tutorials = "content/tutorials/";
var styleGuide = "content/styleGuide/";


var treeWalker = new Walker(root);

// Now define the navigation map
root.addChild(new Node(intro + "structure_and_complexity.htm"));
root.addChild(new Node(intro + "firstProgram.htm"));
root.addChild(new Node(intro + "language_elements.htm"));
root.addChild(new Node(intro + "functionsFirstLook.htm"));
root.addChild(new Node(intro + "programming_environment.htm"));
root.addChild(new Node(variables + "variables.htm"));
root.addChild(new Node(variables + "variablesUsing.htm"));
root.addChild(new Node(variables + "expressions.htm"));
root.addChild(new Node(controls + "if_statements.htm"));
root.addChild(new Node(controls + "while.htm"));
root.addChild(new Node(controls + "logical.htm"));
root.addChild(new Node(functions + "pass_by_reference.htm"));
// root.addChild(new Node(functions + "functions.htm"));
var scope = new Node(functions + "scope.htm");
root.addChild(scope);
scope.addChild(new Node(functions + "discussion.htm"));
root.addChild(new Node(functions + "function_design.htm"));
//root.addChild(new Node(oddNsods + "misc.htm"));
root.addChild(new Node(controls + "forLoops.htm"));
root.addChild(new Node(arrays + "arrays.htm"));
root.addChild(new Node(arrays + "matrices.htm"));
root.addChild(new Node(graphics + "pictures.htm"));
root.addChild(new Node(arrays + "sorting.htm"));
root.addChild(new Node(strings + "strings.htm"));
root.addChild(new Node(streams + "files.htm"));
root.addChild(new Node(styleGuide + "styleGuide.htm"));
//root.addChild(new Node(notes + "calling_sequence.htm"));
//root.addChild(new Node(tutorials + "tutorial1.htm"));
//root.addChild(new Node(tutorials + "tutorial2.htm"));







