package tm.gwt.shared;

import java.util.UUID;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.gwt.state.MirrorState ;
import tm.interfaces.TMStatusCode;

public class TMServiceResult implements IsSerializable {
    /** The GUID. This should be the same as the guid in the request
     * if there is one.  For createEvaluator, the guid is the guid of
     * the new evaluator. If the createEvaluator fails to create a new
     * evaluator, this field should be null. */
    public String guid ;
    
    /** @See tm.interfaces.TMStatusCode.
     * The value is TMStatusCode.NO_EVALUATOR if the GUID is
     * not a valid or expired.
     * The value should never be BUSY_EVALUATING. */
    public int statusCode ; 
    
    /** A short one line message reflecting the status. */
    public String statusMessage ; 
    
    /** A multiline message to show the user.
     * This field should be null if there is no message.*/
    public String attentionMessage ;
    
    /** Additional information from an exception.
     * This field should be null if attention message is null
     * or if the attention message was not accompanied by a Throwable.*/
    public String exceptionInformation ;
    
    /** The state of the machine after the request.
     * This field should never be null. */
    public MirrorState resultState ;
    
    public TMServiceResult(String guid){
    	this.guid = guid;
    	statusCode = TMStatusCode.NO_EVALUATOR;
    	statusMessage = "Evaluator not created";
    	attentionMessage = null;
    	exceptionInformation = null;
    	resultState = new MirrorState();   	
    }
}
