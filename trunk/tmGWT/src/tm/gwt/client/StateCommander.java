package tm.gwt.client;

/** See {@link tm.interfaces.ExternalCommandInterface} for
 * meanings. */
// TODO. Get rid of this interface if it's not really being used.
public interface StateCommander {
    
    public void go( String commandString ) ;
    
    public void goForward();
    
    public void goBack();
    
    public void intoExp(); 
    
    public void overAll() ; 
    
    public void intoSub();
    
    public void toCursor( String fileName, int cursor ) ;
}
