package tm.portableDisplays;

import telford.common.Font ; 
import tm.interfaces.AsserterI ;

public interface PortableContextInterface {
    
    public Font getCodeFont();
    
    public Font getDisplayFont();
    
    public int getHighlightColor();
    
    public AsserterI getAsserter() ;
    
    public void log( String message ) ;
}
