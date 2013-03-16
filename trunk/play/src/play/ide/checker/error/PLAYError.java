package play.ide.checker.error;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class PLAYError {
	
	private String message;
	
    public PLAYError( String message ) {
    	this.message = message ; 
    }
    
    public String getMessage() { 
    	return message ; 
    }
}
