/// <reference path="JSTM.ts" />
/// <reference path="MockJSTM.ts" />
/// <reference path="addTM.ts" />

class FITEController {
    enum FITEControllerState {
        init = 1, started = 2 }

    var _tm : jstm.JSTM ;
    var _startButton : HTMLElement ;
    var _expression : HTMLElement ;
    var _valueBoxes = new Array<HTMLElement>() ;
    var _code : string

    var state : FITEControllerState = FITEControllerState.init ;

    constructor( tm : jstm.JSTM,
                 startButton : HTMLElement,
                 expression : HTMLElement,
                 valueBoxes : Array<HTMLElement>,
                 code : string ) {
        this._tm = tm ;
        this._startButton = startButton ;
        this._expression = expression ;
        valueBoxes.forEach( (el) => this._valueBoxes.push(el) ) ;
        this._code = code ;

        // Add event handlers
        this._startButton.onclick = () => this.start() ;
        this._valueBoxes.forEach( (el) =>
                el.onchange = (() => this.change() ) ) ;
        this._expression.onchange = () => this.change() ;
    }

    start() : void {
        if( state ==  FITEControllerState.init ) {
            this._tm.loadString( newCode, this._language ) ;
            var status = 

}

function go() : void {
    var tm : jstm.JSTM = new jstm.MockJSTM() ;
    var root : HTMLElement = document.documentElement ;
    jstm.addTM( tm, root ) ;
}
