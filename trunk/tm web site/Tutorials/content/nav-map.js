// JavaScript Document
// Assumes tocNavigator.js loaded

var root = new Node("content/titlePage.htm");

// Site Navigation map
// define the directories for convenience

var treeWalker = new Walker(root);

// Now define the navigation map
root.addChild(new Node("content/plug-in-system.html"));
root.addChild(new Node("content/datums/datums.htm"));
root.addChild(new Node("content/configuration/configurations.htm"));
root.addChild(new Node("content/internalScripting.htm"));
root.addChild(new Node("content/externalScripting.htm"));
root.addChild(new Node("content/dataVisualizerAdapter.htm"));







