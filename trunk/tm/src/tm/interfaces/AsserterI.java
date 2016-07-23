package tm.interfaces;

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
    
    
    public void error(String message);
    
}
