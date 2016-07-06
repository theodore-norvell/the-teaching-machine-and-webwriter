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

package tm.utilities;

import java.text.MessageFormat;

import tm.interfaces.SourceCoords;
import tm.portableDisplays.SuperAssert;


public class Assert extends SuperAssert{
    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public static void check(Throwable e ) {
        throw new AssertException(e) ;   }
    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public static void check(boolean proposition, Throwable e ) {
        if( ! proposition ) check( e ) ;   }
    
    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public static void check(boolean proposition ) {
        if( ! proposition ) check( "" ) ;	}

    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public static void check(boolean proposition, String message ) {
        if( ! proposition ) check( message ) ; }

    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public static void check( String message ) {
        throw new AssertException("Assertion failed "+message);	}

    /**
     * Throws an <code>AssertException</code> containing the message
     * provided with argument inserted, if <code>proposition</code> is false.
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param arg the argument to insert
     */
    public static void check( boolean proposition, String message,
                                String arg) {
        check (proposition, message, new Object [] { arg });
    }

    /**
     * Throws an <code>AssertException</code> containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void check( boolean proposition, String message,
                                Object [] args) {
        check (proposition, (new MessageFormat( message ).format( args )));
    }

    /**
     * Throws an <code>AssertException</code> containing the message
     * provided with arguments inserted.
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void check( String message, Object [] args ) {
        check( false, message, args );
    }

    /**
     * Throws an <code>AssertException</code> containing the message
     * provided with argument inserted.
     * @param message the (raw) message text
     * @param arg the argument to insert
     */
    public static void check( String message, String arg ) {
        check( false, message, new Object [] { arg });
    }


    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     * @param coords the source co-ordinates that locate the message
     */

    // Even though it's not the most efficient, the most complex case
    // is coded directly to avoid ambiguity in calling
    // from other cases when one or more arguments are null
    public static void error( boolean proposition, String message,
                                Object [] args, SourceCoords coords ) {
        if (proposition) return;
        message = (args == null) ? message : new MessageFormat( message ).format( args );
        CompileException aarg = new CompileException("Compilation error: " + message);
        if (coords != null) {
            aarg.setSourceCoords(coords);
        }
        throw aarg;
    }

    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     * provided with arguments inserted.
     * @param message the (raw) message text
     * @param args the arguments to insert
     * @param coords the source co-ordinates that locate the message
     */
    public static void error( String message, Object [] args , SourceCoords coords ) {
        error( false, message, args, coords);
    }

    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     * provided with arguments inserted.
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void error( String message, Object [] args  ) {
        error( false, message, args, null);
    }

    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     *  @param proposition If FALSE, there is an error.
     *  @param message The error message.
     *  @param coords The location of the error.
     */
    public static void error(boolean proposition, String message, SourceCoords coords ) {
        error( proposition, message, null, coords ) ; }

    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     *  @param proposition If FALSE, there is an error.
     *  @param message The error message.
     */
    public static void error(boolean proposition, String message) {
        error( proposition, message, null, null) ; }

    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     *  @param message The error message.
     */
    public static void error(String message) {
        error( false, message, null, null) ; }

    /**
     * Throws an <code>CompileException</code> for compilation error containing the message
     *  @param message The error message.
     *  @param coords The location of the error.
     */
    public static void error( String message, SourceCoords coords ) {
        error(false, message, null, coords); }

    /** Apologize for a short coming of the TM.
     *  For example a language construct that is not
     *  implemented. Also if a resource is exhausted.
     */
    public static void apology( boolean proposition, String message ) {
        if( ! proposition ) {
            throw new ApologyException("Apology: "+message) ; } }

    /** Apologize for a short coming of the TM.
     *  For example a language construct that is not
     *  implemented. Also if a resource is exhausted.
     */
    public static void apology( String message ) {
        apology( false, message ) ; }

    /** Specific apology for a language construct that is not
     *  implemented.
     */
    public static void unsupported( String message ) {
        unsupported(message, false) ; }

    /** Specific apology for a language construct that is not
     *  implemented.
     */
    public static void unsupported(boolean proposition, String message ) {
    	if (!proposition) unsupported(message, false) ; }

    /** Specific apology for a language construct that is not
     *  implemented.
     */
    public static void unsupported(boolean proposition, String message, boolean yet ) {
    	if (!proposition) unsupported(message, yet) ; }

   /** Specific apology for a language construct that is not
     *  implemented.
     */
    public static void unsupported( String message, boolean yet ) {
        apology("the Teaching Machine does not support " + message
        			+ (yet ? "yet." : ".") ); }

    /**
     * Throws an <code>ApologyException</code> containing the message
     * provided with arguments inserted.
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void apology( String message, Object [] args ) {
        apology( false, message, args );
    }

    /**
     * Throws an <code>ApologyException</code> containing the message
     * provided with argument inserted.
     * @param message the (raw) message text
     * @param arg the argument to insert
     */
    public static void apology( String message, String arg ) {
        apology( false, message, new Object [] { arg });
    }

    /**
     * Throws an <code>ApologyException</code> containing the message
     * provided with argument inserted, if <code>proposition</code> is false.
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param arg the argument to insert
     */
    public static void apology( boolean proposition, String message,
                                String arg) {
        apology (proposition, message, new Object [] { arg });
    }

    /**
     * Throws an <code>ApologyException</code> containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void apology( boolean proposition, String message,
                                Object [] args) {
        apology (proposition, (new MessageFormat( message ).format( args )));
    }
    
    public static void toBeDone( ) { apology(false, "To be done" ) ; }
    
    /**
     * Throws a <code>RunTimeException</code> containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * runTime exceptions should only be thrown if there is a run time
     * error in the target code
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     * @param coords source co-ordinates where the error occurred
     */
    
    public static void runTimeError( boolean proposition, String message,
                                Object [] args, SourceCoords coords ) {
        if( ! proposition ) {
	        message = (args == null) ? message : new MessageFormat( message ).format( args );
	        TMException aarg = new RunTimeException("Run-time error: " + message);
	        if (coords != null) {
	            aarg.setSourceCoords(coords);
	        }
	        throw aarg;
        }
    }

    /**
     * Throws a <code>RunTimeException</code> containing the message
     * provided , if <code>proposition</code> is false.
     * runTime exceptions should only be thrown if there is a run time
     * error in the target code
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     */
    public static void runTimeError( boolean proposition, String message ) {
    	runTimeError(proposition, message, null, null) ;
    }
    
    /**
     * Throws a <code>RunTimeException</code> containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * runTime exceptions should only be thrown if there is a run time
     * error in the target code
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void runTimeError( boolean proposition, String message,
    		Object [] args) {
    	runTimeError(proposition, message, args, null) ;
    }
    
    /**
     * Throws a <code>RunTimeException</code> containing the message
     * provided.
     * runTime exceptions should only be thrown if there is a run time
     * error in the target code
     * @param message the (raw) message text
     * @param coords source co-ordinates where the error occurred
     */
   public static void runTimeError( String message, SourceCoords coords ) {
        runTimeError(false, message, null, coords) ;
    }
   
   /**
    * Throws a <code>RunTimeException</code> containing the message
    * provided.
    * runTime exceptions should only be thrown if there is a run time
    * error in the target code
    * @param message the (raw) message text
    */
    public static void runTimeError( String message ) {
        runTimeError(false, message, null, null) ;
    }

    /**
     * Throws a <code>ScriptingException</code> containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * Scripting exceptions should only be thrown if there is a runTime
     * error in the scripting code
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     * @param coords source co-ordinates where the error occurred
     */
    
    public static void scriptingError( boolean proposition, String message,
                                Object [] args, SourceCoords coords ) {
        if( ! proposition ) {
	        message = (args == null) ? message : new MessageFormat( message ).format( args );
	        TMException aarg = new RunTimeException("Scripting error: " + message);
	        if (coords != null) {
	            aarg.setSourceCoords(coords);
	        }
	        throw aarg;
        }
    }

    /**
     * Throws a <code>ScriptingException</code> containing the message
     * provided , if <code>proposition</code> is false.
     * Scripting exceptions should only be thrown if there is a run time
     * error in the scripting code
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     */
    public static void scriptingError( boolean proposition, String message ) {
    	scriptingError(proposition, message, null, null) ;
    }
    
    /**
     * Throws a <code>ScriptingException</code> containing the message
     * provided with arguments inserted, if <code>proposition</code> is false.
     * Scripting exceptions should only be thrown if there is a run time
     * error in the scripting code
     * @param proposition if false, throw exception
     * @param message the (raw) message text
     * @param args the arguments to insert
     */
    public static void scriptingError( boolean proposition, String message,
    		Object [] args) {
    	scriptingError(proposition, message, args, null) ;
    }
    
    /**
     * Throws a <code>ScriptingException</code> containing the message
     * provided.
     * Scripting exceptions should only be thrown if there is a run time
     * error in the scripting code
     * @param message the (raw) message text
     * @param coords source co-ordinates where the error occurred
     */
   public static void scriptingError( String message, SourceCoords coords ) {
	   scriptingError(false, message, null, coords) ;
    }
   
   /**
    * Throws a <code>ScriptingException</code> containing the message
    * provided.
    * Scripting exceptions should only be thrown if there is a run time
    * error in the scripting code
    * @param message the (raw) message text
    */
    public static void scriptingError( String message ) {
    	scriptingError(false, message, null, null) ;
    }
}
