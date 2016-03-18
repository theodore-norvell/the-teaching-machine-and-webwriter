/// <reference path="JSTM.ts" />
/// <reference path="MockJSTM.ts" />
/// <reference path="addTM.ts" />
var tm = null;
function go() {
    tm = new jstm.MockJSTM();
    var root = document.documentElement;
    jstm.addTM(tm, root);
}


function getCurrentState() {
    var cState = tm.getCState();
    return cState;
}
function updateExp(exp, tempC, tempF) {
    tm.updateGlobalVariables(0, exp);
    tm.updateGlobalVariables(1, tempC);
    tm.updateGlobalVariables(2, tempF);
}
function testJsType() {
	alert("1");
	var myType = new mun.client.TestJsType();
	alert("2");
	   if (myType.bool) {
	       alert("Ask me what's the meaning of life...");
	   }
}
//# sourceMappingURL=go.js.map