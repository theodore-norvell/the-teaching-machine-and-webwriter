package tm.gwt.client;

import com.google.gwt.core.client.GWT ;
import com.google.gwt.user.client.rpc.AsyncCallback ;
import com.google.gwt.user.client.rpc.ServiceDefTarget ;

import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMService30IntfAsync ;

public class TMServiceAdapter {
    private TMService30IntfAsync proxy ;
    private ServiceDefTarget endpoint ;
    
    TMServiceAdapter( String url ) {
        this.proxy = GWT.create(TMService30Intf.class) ;
        this.endpoint = (ServiceDefTarget)( this.proxy );
        endpoint.setServiceEntryPoint( url );
        // TODO. Check if we need any code to handle errors
    }
    
    void ping() {
        proxy.ping( new PingCallback() ) ;
    }

    private class PingCallback implements AsyncCallback<String> {

        @Override
        public void onFailure(Throwable caught) {
            System.out.println("Ping Failed") ;
        }

        @Override
        public void onSuccess(String result) {
            GWT.log("Ping Succeeded with " + result ) ;
        }
    }
}
