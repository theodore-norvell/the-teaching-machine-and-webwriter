package tm.gwt.server;

import tm.gwt.shared.EvaluatorWrapper;
import tm.gwt.shared.TMService30Intf ;
import tm.gwt.shared.TMServiceResult ;
import tm.gwt.state.MirrorState ;
import tm.interfaces.TMStatusCode ;
import java.util.HashMap;
import java.util.UUID;
import com.google.gwt.user.server.rpc.RemoteServiceServlet ;

public class TMService30 extends RemoteServiceServlet
    implements TMService30Intf {

	private HashMap<String, EvaluatorWrapper> evaluators = new HashMap<String, EvaluatorWrapper>();

    @Override
    public String ping() {
        return "TMService30 is operating" ;
    }

    @Override
    public TMServiceResult createEvaluator() {
        System.out.println( "In  createEvaluator ") ;
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.exceptionInformation = null ;
        result.attentionMessage = null ;
        EvaluatorWrapper evaluator = new EvaluatorWrapper();
        String guid = UUID.randomUUID().toString();
        evaluators.put(guid, evaluator);
        result.guid = guid;
        result.resultState = new MirrorState();
        result.statusCode = TMStatusCode.READY;
        result.statusMessage = "Evaluator is already created." ;
        return result;
    }

    @Override
    public TMServiceResult loadString(String guid, String fileName,
            String programSource) {
        System.out.println( "In  loadString" );
        TMServiceResult result = new TMServiceResult() ;
        evaluators.get(guid).loadString(fileName, programSource);
        return result ;
    }

    @Override
    public TMServiceResult loadRemoteFile(String guid, String root,
            String fileName) {
        System.out.println( "In  loadRemoteFile" );
        TMServiceResult result = new TMServiceResult() ;
        evaluators.get(guid).loadRemoteFile(root, fileName);
        return result ;
    }

    @Override
    public TMServiceResult initializeTheState(String guid) {
        System.out.println( "In  initializeTheState" );
        TMServiceResult result = new TMServiceResult() ;
        evaluators.get(guid).initializeTheState();
        return result ;
    }

    @Override
    public TMServiceResult go(String guid, String commandString) {
        System.out.println( "In  go" );
        TMServiceResult result = new TMServiceResult() ;
        evaluators.get(guid).go(commandString);
        return result ;
    }

    @Override
    public TMServiceResult goBack(String guid) {
        System.out.println( "In  goBack" );
        TMServiceResult result = new TMServiceResult() ;
        evaluators.get(guid).goBack();
        return result ;
    }

    @Override
    public TMServiceResult toCursor(String guid, String fileName, int cursor) {
        System.out.println( "In  toCursor" );
        TMServiceResult result = new TMServiceResult() ;
        evaluators.get(guid).toCursor(fileName, cursor);
        return result ;
    }

}
