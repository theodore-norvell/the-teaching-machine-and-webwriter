/// <reference path="JSTM.ts" />
/// <reference path="MockJSTM.ts" />
/// <reference path="addTM.ts" />

function go() : void {
    var tm : jstm.JSTM = new jstm.MockJSTM() ;
    var root : HTMLElement = document.documentElement ;
    jstm.addTM( tm, root ) ;
}
