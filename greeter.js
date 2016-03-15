/**
 * New typescript file
 */
function updateState() {
	var selectvalue = document.getElementById("state").value;
	var name = getWindowName(selectvalue);
	document.getElementById("exp-q42").value = name;
}
function createCanvas() {
	makeCanvasOnPanel();
}
function drawCircle() {
	drawCircleOnPanel("black");
}
function addLabel() {
	makeLabel1("MyLabel");
}

function simpleTest() {
	drawJSChart();
}

function classify(arg) {

	return Object.prototype.toString.call(arg);
}

function show() {
	var testButton = makeButton();
	var result = classify(testButton);
	alert(result);
}

function go() {
	var root = document.documentElement;
	walk(root);
}

function walk(node) {
	var name = node.getAttribute("name");
	var newElem = null;
	switch (name) {
		case "tm-goBack":
			newElem = makeButton("GoBack");//makeGoBackButton();//
			newElem.setAttribute("class", "tm-button");
			newElem.innerHTML = "&lt;-";
			break;
		case "tm-goForward":
			newElem = makeButton("GoForward");//makeGoBackButton();//
			newElem.setAttribute("class", "tm-button");
			newElem.innerHTML = "-&gt;";
			break;
		case "tm-expDisp":
			newElem = makeDivElement();//makeGoBackButton();//
			break;
		case "canvasFromGWT":
			//Canvas created from Javascript
//			newElem = document.createElement("canvas");
//			newElem.width = 200;
//			newElem.height = 100;
//			newElem.setAttribute("class", "tm-canvas");
			newElem = getCanvas();
			//Canvas returned from GWT JSNI
			break;
	}
	if (newElem != null){
		replace(node, newElem);
	}
	var children = node.childNodes;
	var childArray = new Array();
	var i;
	var j = 0;
	for (i = 0; i < children.length; ++i) {
		if (children.item(i).nodeType == document.ELEMENT_NODE) {
			childArray[j++] = children.item(i);
		}
	}
	// Recurse on the children
	for (i = 0; i < childArray.length; ++i) {
		walk(childArray[i]);
	}
}

function replace(oldElem, newElem) {
    oldElem.parentNode.replaceChild(newElem, oldElem);
}

function makeGoBackButton() {
//    var _this = this;
    var button = document.createElement("button");
    button.setAttribute("class", "tm-button");
//    button.onclick = function () { return _this.goBack(); };
    button.innerHTML = "&lt;-";
    return button;
}

// # sourceMappingURL=greeter.js.map
