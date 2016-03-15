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
//# sourceMappingURL=go.js.map