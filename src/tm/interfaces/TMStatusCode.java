package tm.interfaces;

public interface TMStatusCode {

    /** Status Code -- No Evaluator is present. This means no display manager either. */
    public static final int NO_EVALUATOR = 0 ;
    
    /**  Status Code -- An evaluator is built but compilation hasn't happened yet.  */
    public static final int READY_TO_COMPILE = 1 ;
    
    /**  Status Code -- Compilation failed. */
    public static final int DID_NOT_COMPILE = 2 ;
    
    /**  Status Code -- Compilation was successful. Initialization has not been done.*/
    public static final int COMPILED = 3 ;
    
    /**  Status Code -- Ready for next step*/
    public static final int READY = 4 ;
    
    /**  Status Code -- The evaluator is busy executing code. This state is transitory
     * in that the TM should not be in this state while awaiting user input (e.g. button pushes).
     * However it will be in this state while any evaluation is going on. So in particular
     * any call-backs from script code embedded in interpreted code will find the TM in this
     * state.  The TMBigApplet might find itself in this state if it is invoked by a call-back.
     */
    public static final int BUSY_EVALUATING = 5 ;
    
    /**  Status Code -- The program has run to completion. */
    public static final int EXECUTION_COMPLETE = 6 ;
    
    /**  Status Code -- The program has crashed or otherwise completed in an unpleasant way. */
    public static final int EXECUTION_FAILED = 7 ; 

}
