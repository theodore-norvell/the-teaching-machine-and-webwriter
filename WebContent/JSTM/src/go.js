/// <reference path="JSTM.ts" />
/// <reference path="MockJSTM.ts" />
/// <reference path="addTM.ts" />
function go() {
    var tm = new jstm.MockJSTM();
    var root = document.documentElement;
    jstm.addTM(tm, root);
}
