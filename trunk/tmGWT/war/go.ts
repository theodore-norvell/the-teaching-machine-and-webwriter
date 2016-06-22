/// <reference path="GWTJSTM.ts" />
var tm: jstm.GWTJSTM;
function init(): void {
    tm = new jstm.GWTJSTM();
    tm.init();
    //    tm.updateExpression();
    tm.setMirrorState();
}

function updateValue(): void {
    tm.test("\ufffd\ufffci = \uffff1");//"\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ");
}