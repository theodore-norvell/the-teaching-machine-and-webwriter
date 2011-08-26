//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/*
 * Created on Apr 1, 2005.  No kidding.
 *
 */
package tm.interfaces;

/**
 * @author theo
 *
 */
public interface StatusProducer {

    /** Status Code -- There is no file loaded into the TM */
    public static final int NO_FILE_LOADED = 0 ;
    
    /**  Status Code -- A file is loaded and compiled. The state is not initialized.  */
    public static final int FILE_LOADED = 2 ;
    
    /**  Status Code -- The state has been initialized and the TM is ready for a go-forward type command */
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
    

    /** Return a status code indicating the current status of the TM.
     * @see tm.interfaces.StatusConsumer */
    public int getStatusCode() ;
    
    /** Return a user friendly message explaining why that status is what it is.
     * In the case of a compiler error or an execution error, it should be the
     * text of the message.
     */
    public String getStatusMessage() ;

}
