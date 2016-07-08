module jstm {
    
    export interface EvaluatorInterface {
        
        getExpression : () => string ;

        getNumConsoleLines : () => number ;

        getConsoleLine : (i : number) => string ;

        getNumSelectedCodeLines : (tmf : TMFileI, allowGaps : boolean ) => number ;

        getSelectedCodeLine : (tmf : TMFileI, allowGaps : boolean, i : number) => CodeLineI ;
    }
}
