package tm.interfaces;

import java.text.MessageFormat ;

import tm.utilities.ApologyException ;
import tm.utilities.AssertException ;
import tm.utilities.CompileException ;
import tm.utilities.RunTimeException ;
import tm.utilities.TMException ;

public interface AsserterI {

    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public void check(Throwable e ) ;
    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public void check(boolean proposition, Throwable e ) ;
    
    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public void check(boolean proposition ) ;

    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public void check(boolean proposition, String message ) ;

    /** Check for internal consistency.
     *  Assert.checks should only be thrown the TM is behaving in a way it shouldn't
     */
    public void check( String message ) ;
    
    /** Code not yet written. */
    public void toBeDone( ) ;
    
}
