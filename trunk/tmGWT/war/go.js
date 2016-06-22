/// <reference path="GWTJSTM.ts" />
var tm;
function init() {
    tm = new jstm.GWTJSTM();
    tm.init();
    //    tm.updateExpression();
    tm.setMirrorState();
}
function updateValue() {
    tm.test("\ufffd\ufffci = \uffff1"); //"\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ");
}
//# sourceMappingURL=go.js.map