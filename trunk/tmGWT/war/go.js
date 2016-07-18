/// <reference path="GWTJSTM.ts" />
var tm;
function init() {
    tm = new jstm.GWTJSTM();
    tm.init();
    //    tm.updateExpression();
    tm.setMirrorState();
}
function updateValue() {
    tm.updateExpression("\ufffd\ufffci = \uffff1");
//    alert(tm.getExpression());
}
//# sourceMappingURL=go.js.map