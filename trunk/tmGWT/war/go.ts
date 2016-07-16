/// <reference path="GWTJSTM.ts" />
var tm: jstm.GWTJSTM;
function init(): void {
    tm = new jstm.GWTJSTM();
    tm.init();
    //    tm.updateExpression();
    tm.setMirrorState();
}

function updateValue(): void {
    tm.updateExpression("\ufffd\ufffci = \uffff1");
}