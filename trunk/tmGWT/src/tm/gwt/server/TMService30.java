package tm.gwt.server;

import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMServiceResult ;

import com.google.gwt.user.server.rpc.RemoteServiceServlet ;

public class TMService30 extends RemoteServiceServlet
    implements TMService30Intf{

    @Override
    public String ping() {
        return "TMService30 is operating" ;
    }

    @Override
    public TMServiceResult createEvaluator() {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public TMServiceResult loadString(String guid, String fileName,
            String programSource) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public TMServiceResult loadRemoteFile(String guid, String root,
            String fileName) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public TMServiceResult initializeTheState(String guid) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public TMServiceResult go(String guid, String commandString) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public TMServiceResult goBack(String guid) {
        // TODO Auto-generated method stub
        return null ;
    }

}
