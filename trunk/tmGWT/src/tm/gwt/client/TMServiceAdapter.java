package tm.gwt.client;

import com.google.gwt.core.client.GWT ;
import com.google.gwt.user.client.rpc.AsyncCallback ;
import com.google.gwt.user.client.rpc.ServiceDefTarget ;

import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMService30IntfAsync ;
import tm.gwt.shared.TMServiceResult ;
import tm.gwt.shared.state.MirrorState ;
import tm.interfaces.StateFormatter ;

public class TMServiceAdapter extends Observable implements StateCommander {
    private TMService30IntfAsync proxy ;
    private ServiceDefTarget endpoint ;
    private TMServiceResult latestResult ;
    private MirrorState mirrorState ;
    private Callback callback = new Callback() ;
    
    TMServiceAdapter( String url, MirrorState mirrorState ) {
        this.mirrorState = mirrorState ;
        this.latestResult = new TMServiceResult( null ) ;
        this.latestResult.statusMessage = "Server not yet contacted." ;
        this.proxy = GWT.create(TMService30Intf.class) ;
        this.endpoint = (ServiceDefTarget)( this.proxy );
        endpoint.setServiceEntryPoint( url );
    }
    
    public String getAttentionMessage() {
        return this.latestResult.attentionMessage ;
    }
    
    public String getExceptionInformation() {
        return this.latestResult.exceptionInformation ;
    }
    
    public int getStatusCode() {
        return this.latestResult.statusCode ;
    }
    
    public String getStatusMessage() {
        return this.latestResult.statusMessage ;
    }
    
    public void ping() {
        proxy.ping( new PingCallback() ) ;
    }
    
    public void createEvaluator(int language ) {
        GWT.log( "createEvaluator sent to proxy" );
        proxy.createEvaluator(language, callback ) ;
    }
    
    public void loadString( String fileName, String programSource) {
        if( this.latestResult.guid != null ) {
            GWT.log( "loadString sent to proxy" );
            proxy.loadString( this.latestResult.guid, fileName, programSource, callback );
        }
    }
    
    public void loadRemoteFile( String root, String fileName ) {
        if( this.latestResult.guid != null ) {
            GWT.log( "loadRemoteFile sent to proxy" );
            proxy.loadRemoteFile( this.latestResult.guid, root, fileName, callback );
        }
    }
    
    public void initializeTheState( ) {
        if( this.latestResult.guid != null ) {
            GWT.log( "initializeTheState sent to proxy" );
            proxy.initializeTheState( this.latestResult.guid, callback );
        }
    }
    
    @Override
    public void go( String commandString ) {
        if( this.latestResult.guid != null ) {
            GWT.log( "go sent to proxy" );
            proxy.go( this.latestResult.guid, commandString, callback );
        }
    }
    
    @Override
    public void goBack() {
        if( this.latestResult.guid != null ) {
            GWT.log( "goBack sent to proxy" );
            proxy.goBack( this.latestResult.guid, callback );
        }
    }

    @Override
    public void goForward() { go( "f" ) ; }

    @Override
    public void intoExp() { go( "e" ) ; }

    @Override
    public void overAll() { go( "o") ; }

    @Override
    public void intoSub() { go( "s" ) ; }

    @Override
    public void toCursor(String fileName, int cursor) {
        if( this.latestResult.guid != null ) {
            proxy.toCursor( this.latestResult.guid, fileName, cursor, callback );
        }
    }

    private class Callback implements AsyncCallback<TMServiceResult> {
        @Override
        public void onFailure(Throwable caught) {
            GWT.log("Failure") ;
            TMServiceAdapter.this.latestResult.attentionMessage =
                 "Communication with server failed." ;
            // TODO. For the exceptionInformation, we really need
            // the full stack trace. See tm.AttentionFrame.
            TMServiceAdapter.this.latestResult.exceptionInformation =
                        caught.getMessage() ;
            TMServiceAdapter.this.setChanged();
            TMServiceAdapter.this.notifyObservers();
        }

        @Override
        public void onSuccess(TMServiceResult result) {
            GWT.log("Success") ;
            TMServiceAdapter.this.latestResult = result ;
            /*DGB*/ GWT.log("New state from server is"); /*DBG*/
            /*DGB*/ StringBuffer b = new StringBuffer() ; /*DBG*/
            /*DGB*/ StateFormatter.formatState( result.resultState, b, "    |" ) ; /*DBG*/
            /*DGB*/ GWT.log( b.toString() ) ; /*DBG*/
            /*DGB*/ GWT.log("Before update, mirrorState is"); /*DBG*/
            /*DGB*/ b = new StringBuffer() ; /*DBG*/
            /*DGB*/ StateFormatter.formatState( mirrorState, b, "    |" ) ; /*DBG*/
            /*DGB*/ GWT.log( b.toString() ) ; /*DBG*/
            mirrorState.update( result.resultState ) ;
            /*DGB*/ GWT.log("After update, mirrorState is"); /*DBG*/
            /*DGB*/ b = new StringBuffer() ; /*DBG*/
            /*DGB*/ StateFormatter.formatState( mirrorState, b, "    |" ) ; /*DBG*/
            /*DGB*/ GWT.log( b.toString() ) ; /*DBG*/
            
            /*DGB*/ GWT.log("After update, console lines are"); /*DBG*/
            /*DGB*/ b = new StringBuffer() ; /*DBG*/
            /*DGB*/ StateFormatter.formatConsoleLines( mirrorState, b, "    |" ) ; /*DBG*/
            /*DGB*/ GWT.log( b.toString() ) ; /*DBG*/
            
            TMServiceAdapter.this.setChanged();
            TMServiceAdapter.this.notifyObservers();
        }
    }

    private class PingCallback implements AsyncCallback<String> {

        @Override
        public void onFailure(Throwable caught) {
            GWT.log("Ping Failed") ;
        }

        @Override
        public void onSuccess(String result) {
            GWT.log("Ping Succeeded with " + result ) ;
        }
    }
}
