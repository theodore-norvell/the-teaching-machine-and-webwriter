package tm.gwt.server;

import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMServiceResult ;
import tm.gwt.state.MirrorState ;
import tm.interfaces.TMStatusCode ;

import com.google.gwt.core.shared.GWT ;
import com.google.gwt.user.server.rpc.RemoteServiceServlet ;

public class TMService30 extends RemoteServiceServlet
    implements TMService30Intf{

    @Override
    public String ping() {
        return "TMService30 is operating" ;
    }

    @Override
    public TMServiceResult createEvaluator() {
        System.out.println( "In  createEvaluator ") ;
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = "Could not create Evaluator" ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

    @Override
    public TMServiceResult loadString(String guid, String fileName,
            String programSource) {
        System.out.println( "In  loadString" );
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = null ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

    @Override
    public TMServiceResult loadRemoteFile(String guid, String root,
            String fileName) {
        System.out.println( "In  loadRemoteFile" );
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = null ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

    @Override
    public TMServiceResult initializeTheState(String guid) {
        System.out.println( "In  initializeTheState" );
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = null ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

    @Override
    public TMServiceResult go(String guid, String commandString) {
        System.out.println( "In  go" );
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = null ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

    @Override
    public TMServiceResult goBack(String guid) {
        System.out.println( "In  goBack" );
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = null ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

    @Override
    public TMServiceResult toCursor(String guid, String fileName, int cursor) {
        System.out.println( "In  toCursor" );
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.attentionMessage = null ;
        result.exceptionInformation = null ;
        result.guid = null ;
        result.statusCode = TMStatusCode.NO_EVALUATOR ;
        result.statusMessage = "Could not create evaluator." ;
        result.resultState = new MirrorState() ;
        return result ;
    }

}
