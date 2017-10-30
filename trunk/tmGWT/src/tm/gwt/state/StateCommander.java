package tm.gwt.state;

/** See {@link tm.interfaces.ExternalCommandInterface} for
 * meanings. */
public interface StateCommander {
    
    public void go( String commandString ) ;
    
    public void goForward();
    
    public void goBack();
    
    public void intoExp(); 
    
    public void overAll() ; 
    
    public void intoSub();
    
    public void toCursor( String fileName, int cursor ) ;
}
