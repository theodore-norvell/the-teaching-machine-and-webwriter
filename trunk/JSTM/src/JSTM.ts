module jstm {
    export interface JSTM {
        makeExpressionDisplay : () => HTMLElement ;

        makeGoForwardButton : () => HTMLElement ;

        makeGoBackButton : () => HTMLElement ;

        makeVariableWatcher : ( varName : string ) => HTMLElement ;

        loadString : ( program : string, language : string ) => void ;

        getStatus : () => number ;

        go : ( commandString : string ) => void ;

        goForward : () => void ;

        goBack : () => void ; 
    }
}
