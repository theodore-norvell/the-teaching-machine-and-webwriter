package tm.gwt.server;

import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMServiceResult ;
import tm.interfaces.TMStatusCode ;
import java.util.HashMap;
import java.util.UUID;
import com.google.gwt.user.server.rpc.RemoteServiceServlet ;

public class TMService30 extends RemoteServiceServlet
    implements TMService30Intf {

	private HashMap<String, EvaluatorWrapper> wrappers = new HashMap<String, EvaluatorWrapper>();

    @Override
    public String ping() {
        return "TMService30 is operating" ;
    }

    @Override
    public TMServiceResult createEvaluator(int language) {
        System.out.println( "In  createEvaluator ") ;
        TMServiceResult result = new TMServiceResult() ;
        result.exceptionInformation = "" ;
        result.attentionMessage = "" ;
        String guid = UUID.randomUUID().toString();
        result.guid = guid;
        result.statusCode = TMStatusCode.READY_TO_COMPILE ;
        result.statusMessage = "" ;
        TMServiceStatusReporter reporter = new TMServiceStatusReporter( result ) ;
        try {
        	EvaluatorWrapper wrapper = new EvaluatorWrapper(language, reporter );
        	synchronized(wrappers){ wrappers.put(guid, wrapper);}
        }
        catch( Throwable th ) {
            result.statusCode = TMStatusCode.NO_EVALUATOR ;
            result.statusMessage = "Evaluator not created" ;
        }
        return result ;
    }

    @Override
    public TMServiceResult loadString(String guid, String fileName,
            String programSource) {
        System.out.println( "In  loadString" );
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        // TODO Check guid.  And be synchronized.
        TMServiceResult result = new TMServiceResult() ;
        wrapper.loadString(result, fileName, programSource);
        return result ;
    }

    @Override
    public TMServiceResult loadRemoteFile(String guid, String root,
            String fileName) {
        System.out.println( "In  loadRemoteFile" );
        TMServiceResult result = new TMServiceResult() ;
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        wrapper.loadRemoteFile(result, root, fileName);
        return result ;
    }

    @Override
    public TMServiceResult initializeTheState(String guid) {
        System.out.println( "In  initializeTheState" );
        TMServiceResult result = new TMServiceResult() ;
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        wrapper.initializeTheState();
        return result ;
    }

    @Override
    public TMServiceResult go(String guid, String commandString) {
        System.out.println( "In  go" );
        TMServiceResult result = new TMServiceResult() ;
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        wrapper.go(commandString);
        return result ;
    }

    @Override
    public TMServiceResult goBack(String guid) {
        System.out.println( "In  goBack" );
        TMServiceResult result = new TMServiceResult() ;
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        wrapper.goBack();
        return result ;
    }

    @Override
    public TMServiceResult toCursor(String guid, String fileName, int cursor) {
        System.out.println( "In  toCursor" );
        TMServiceResult result = new TMServiceResult() ;
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        wrapper.toCursor(fileName, cursor);
        return result ;
    }

}
