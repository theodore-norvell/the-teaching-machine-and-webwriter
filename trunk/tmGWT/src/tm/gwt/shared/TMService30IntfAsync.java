package tm.gwt.shared;

import com.google.gwt.user.client.rpc.AsyncCallback ;

public interface TMService30IntfAsync {
    
    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void ping( AsyncCallback<String> callback ) ;

    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void createEvaluator( AsyncCallback<TMServiceResult> callback ) ;

    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void loadString(
            String guid, String fileName, String programSource,
            AsyncCallback<TMServiceResult> callback );
    
    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void loadRemoteFile(
            String guid, String root, String fileName,
            AsyncCallback<TMServiceResult> callback ) ;
    
    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void initializeTheState(
            String guid,
            AsyncCallback<TMServiceResult> callback ) ;
    
    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void go(
            String guid, String commandString,
            AsyncCallback<TMServiceResult> callback ) ;
    
    /** See {@link tm.gwt.shared.TMService30Intf} */
    public void goBack(
            String guid,
            AsyncCallback<TMServiceResult> callback ) ;
}
