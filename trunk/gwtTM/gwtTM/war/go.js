/// <reference path="GWTJSTM.ts" />
/// <reference path="MirrorState.ts" />
var tm;
var  numStates = 8;
var k = 0;
function initJSState() {
    tm = new jstm.GWTJSTM();
    tm.createState();
}
function getJsState() {
    var jsState = tm.getState();
    return jsState;
}
function testSetExpression(str) {
    tm.setExpression(str);
    var expNew = tm.getState().getExpression();
    alert("Exp is: " + expNew);
}

//
function onLoad() {
	tm = new jstm.GWTJSTM();
	var root = document.documentElement;
	jstm.addTM(tm, root);
}

function goForward() {
	if(tm == null){
		tm = new jstm.GWTJSTM();
	}
	k = (k + 1) % numStates;
	tm.getState().setExpression(k);
  var x = tm.refresh();
  var root = document.documentElement;
  jstm.addTM(tm, root);
}

function backForward() {
	if(tm == null){
		tm = new jstm.GWTJSTM();
	}
	k = (k - 1) % numStates;
	tm.getState().setExpression(k);
  var x = tm.refresh();
  var root = document.documentElement;
  jstm.addTM(tm, root);
}
//# sourceMappingURL=go.js.map