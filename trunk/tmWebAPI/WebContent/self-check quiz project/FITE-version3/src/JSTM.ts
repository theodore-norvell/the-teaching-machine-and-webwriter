/// <reference path="Promise.ts"/>


module jstm {
    
    import Promise = P.Promise ;
    
    export interface JSTM {
        //the guid
        guid:string;
        
        //the mirrorData object received from the remoteTM.
        mirrorData:any;
        
        //
        status:string;
        message:string;
        programText:string;
        
        goForwardButton:HTMLElement;
        goBackButton:HTMLElement;
        expressionDisplay:HTMLElement;
        VariableWatcher:HTMLElement;
        //jstm hold a reference to the FITEQuestion
        //qz:quizBuilder.FITEQuestion;
      
       //

       /**
        * make a display table
        */
        makeTMDisplay : () => HTMLElement ;

        /** Make a button that will move the TM state forward by
        *   the smallest interesting step.
        * @params
        *    onDone : A callback to be executed after the response from the
        *             TM arrives and the displays have been updated.
        *    onFail : A callback to be executed if the response
        *             from the TM is not HTTP response code 200.
        */
        makeGoForwardButton : (onDone ?: (jstm:JSTM) => void,
                               onFail ?: (r:P.Rejection) => void)
                            => HTMLElement ;
 
        /** Make a button that will undo the last forward step.
        * @params
        *    onDone : A callback to be executed after the response
        *             from the TM arrives and the displays have been updated.
        *    onFail : A callback to be executed if the response
        *             from the TM is not HTTP response code 200.
        */
        makeGoBackButton : (onDone? : (jstm:JSTM) => void,
                            onFail? : (r:P.Rejection) => void)
                         => HTMLElement ;
        
        //
        makeExpressionDisplay: (onDone? : (jstm:JSTM) => void,
                            onFail? : (r:P.Rejection) => void)
                         => HTMLElement ;
        /** Make an HTML element that displays the state of one variable.
        *    This display will update automatically.
        */
        //add a parameter to this method.
        makeVariableWatcher : ( Name : string,initValue:string) => HTMLElement ;
        
        
        //set programText
        setprogramText(qt:string);
        
        //
        getprogramText();
        /** The current status.  See WebAPI documentation for a list */
        getStatus : () => string ;
        //
        setStatus:(status:string)=>void;

        /** The most recent error message from the Remote TM.*/
        getMessage : () => string  ;
        //
        setMessage:(message:string)=>void;
        
        
        initialize:(responseWantedFlag:string)=> Promise<JSTM> ;

        /** Compile a program. See WebAPI documentation for details.
        *   The result is a Promise object. This promise will be resolved when the 
        *   server's response arrives and has HTTP response code 200.  It will be
        *   rejected if the response code is anything else.
        */
        loadString : ( program : string, language : string ) => Promise<JSTM> ;

        /** Advance the state of the TM.  See WebAPI documentation for details.
        *   The result is a Promise object. This promise will be resolved when the 
        *   server's response arrives and has HTTP response code 200.  It will be
        *   rejected if the response code is anything else.
        */
        go : ( commandString : string ) => Promise<JSTM> ;

        /** Same as go("f") ;
        */
        goForward : () => Promise<JSTM> ;

        /** Undo the last advance of the remote TM.
        *   The result is a Promise object. This promise will be resolved when the 
        *   server's response arrives and has HTTP response code 200.  It will be
        *   rejected if the response code is anything else.
        */
        goBack : () => Promise<JSTM> ; 
    }
}