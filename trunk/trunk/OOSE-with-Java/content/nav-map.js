// JavaScript Document
// Assumes tocNavigator.js loaded
// Normal starting page should go here
var root = new Node("content/titlePage.html");
var treeWalker = new Walker(root);


// Site Navigation map
// define the directories for convenience
var intro_dir = "content/0-Intro/";
var objects_dir = "content/1-Objects/";
var poly_dir = "content/2-Polymorphism/";
var further_dir = "content/3-Further-Topics/";


// Now define the navigation map
root.addChild(new Node(intro_dir + "0-introduction.html"));
root.addChild(new Node(intro_dir + "1-primitive-types.html"));
root.addChild(new Node(intro_dir + "2-statements.html"));
root.addChild(new Node(intro_dir + "3-arrays.html"));
root.addChild(new Node(intro_dir + "4-static-methods.html"));

root.addChild(new Node(objects_dir + "0-immutable-objects.html"));
root.addChild(new Node(objects_dir + "1-mutable-objects.html"));

root.addChild(new Node(poly_dir + "0-interfaces.html"));
root.addChild(new Node(poly_dir + "1-extending-classes.html"));
root.addChild(new Node(poly_dir + "2-subtypes.html"));
root.addChild(new Node(poly_dir + "3-implementing-polymorphism.html"));

root.addChild(new Node(further_dir + "0-enumerations.html"));
root.addChild(new Node(further_dir + "1-packages-and-access.html"));
root.addChild(new Node(further_dir + "2-protected-methods.html"));
root.addChild(new Node(further_dir + "3-exceptions.html"));
root.addChild(new Node(further_dir + "4-library-classes.html"));
root.addChild(new Node(further_dir + "5-inner-classes.html"));




