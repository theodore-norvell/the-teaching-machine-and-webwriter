package tm.gwt.client;

import com.google.gwt.core.client.GWT ;
import com.google.gwt.user.client.rpc.AsyncCallback ;
import com.google.gwt.user.client.rpc.ServiceDefTarget ;

import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMService30IntfAsync ;
import tm.gwt.shared.TMServiceResult ;
import tm.gwt.state.MirrorState ;
import tm.gwt.state.StateCommander ;
import tm.interfaces.TMStatusCode ;

public class TMServiceAdapter extends Observable implements StateCommander {
    private TMService30IntfAsync proxy ;
    private ServiceDefTarget endpoint ;
    private TMServiceResult latestResult ;
    private MirrorState mirrorState ;
    private Callback callback = new Callback() ;
    
    TMServiceAdapter( String url, MirrorState mirrorState ) {
        this.mirrorState = mirrorState ;
        this.latestResult = new TMServiceResult() ;
        this.latestResult.attentionMessage = null ;
        this.latestResult.exceptionInformation = null ;
        this.latestResult.guid = null ;
        this.latestResult.statusCode = TMStatusCode.NO_EVALUATOR ;
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
    
    public void ping() {
        proxy.ping( new PingCallback() ) ;
    }
    
    public void createEvaluator( ) {
        proxy.createEvaluator( callback ) ;
    }
    
    public void loadString( String fileName, String programSource) {
        if( this.latestResult.guid != null ) {
            proxy.loadString( this.latestResult.guid, fileName, programSource, callback );
        }
    }
    
    public void loadRemoteFile( String root, String fileName ) {
        if( this.latestResult.guid != null ) {
            proxy.loadRemoteFile( this.latestResult.guid, root, fileName, callback );
        }
    }
    
    public void initializeTheState( ) {
        if( this.latestResult.guid != null ) {
            proxy.initializeTheState( this.latestResult.guid, callback );
        }
    }
    
    @Override
    public void go( String commandString ) {
        if( this.latestResult.guid != null ) {
            proxy.go( this.latestResult.guid, commandString, callback );
        }
    }
    
    @Override
    public void goBack() {
        if( this.latestResult.guid != null ) {
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
            TMServiceAdapter.this.latestResult = result ;
            mirrorState.update( result.resultState ) ;
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
