package tm.gwt.shared;

import com.google.gwt.user.client.rpc.RemoteService ;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath ;

@RemoteServiceRelativePath("tmService30")
public interface TMService30Intf extends RemoteService {
    
    /** Is the service running? */
    public String ping() ;
    
    /** Create a new evaluator. */
    public TMServiceResult createEvaluator(int language) ;

    /** Compile a string. See {@link tm.interfaces.ExternalCommandInterface} */
    public TMServiceResult loadString( String guid, String fileName, String programSource);
    
    /** Compile a remote file. See {@link tm.interfaces.ExternalCommandInterface} */
    public TMServiceResult loadRemoteFile( String guid, String root, String fileName ) ;
    
    /** Initialize the state. See {@link tm.interfaces.ExternalCommandInterface} */
    public TMServiceResult initializeTheState( String guid ) ;
    
    /** Go. See {@link tm.interfaces.ExternalCommandInterface} */
    public TMServiceResult go( String guid, String commandString ) ;
    
    /** Undo. See {@link tm.interfaces.ExternalCommandInterface} */
    public TMServiceResult goBack( String guid ) ;
    
    /** To Cursor. See {@link tm.interfaces.ExternalCommandInterface} */
    public TMServiceResult toCursor(String guid, String fileName, int cursor) ;
}