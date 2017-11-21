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
        String guid = UUID.randomUUID().toString();
        TMServiceResult result = new TMServiceResult(guid) ;
        try {
            	EvaluatorWrapper wrapper = new EvaluatorWrapper(language, result );
            	if( result.statusCode == TMStatusCode.READY_TO_COMPILE ) {
            	    synchronized(wrappers){ 
            	        wrappers.put(guid, wrapper);} } }
        catch( Throwable th ) {
            result.statusCode = TMStatusCode.NO_EVALUATOR ;
            result.statusMessage = "Evaluator not created" ;
        }
        result.statusCode = TMStatusCode.READY_TO_COMPILE ;
        //Is the file ready to compile after evaluator is created?
        return result ;
    }

    @Override
    public TMServiceResult loadString(String guid, String fileName,
            String programSource) {
        System.out.println( "In  loadString" );
        TMServiceResult result = new TMServiceResult(guid) ;
        EvaluatorWrapper wrapper = getWrapper( guid, result ) ;
        if( wrapper != null) {
            wrapper.loadString(result, fileName, programSource);
        }
        return result ;
    }

    @Override
    public TMServiceResult loadRemoteFile(String guid, String root,
            String fileName) {
        System.out.println( "In  loadRemoteFile" );
        TMServiceResult result = new TMServiceResult(guid) ;
        EvaluatorWrapper wrapper = getWrapper( guid, result ) ;
        if( wrapper != null) {
            wrapper.loadRemoteFile(result, root, fileName);
        }
        return result ;
    }

    @Override
    public TMServiceResult initializeTheState(String guid) {
        System.out.println( "In  initializeTheState" );
        TMServiceResult result = new TMServiceResult(guid) ;
        EvaluatorWrapper wrapper = getWrapper( guid, result ) ;
        if( wrapper != null) {
            wrapper.initializeTheState(result); }
        return result ;
    }

    @Override
    public TMServiceResult go(String guid, String commandString) {
        System.out.println( "In  go" );
        TMServiceResult result = new TMServiceResult(guid) ;
        EvaluatorWrapper wrapper = getWrapper( guid, result ) ;
        if( wrapper != null) {
            wrapper.go(result, commandString); }
        return result ;
    }

    @Override
    public TMServiceResult goBack(String guid) {
        System.out.println( "In  goBack" );
        TMServiceResult result = new TMServiceResult(guid) ;
        EvaluatorWrapper wrapper = getWrapper( guid, result ) ;
        if( wrapper != null) {
            wrapper.goBack(result);
        }
        return result ;
    }

    @Override
    public TMServiceResult toCursor(String guid, String fileName, int cursor) {
        System.out.println( "In  toCursor" );
        TMServiceResult result = new TMServiceResult(guid) ;
        EvaluatorWrapper wrapper = getWrapper( guid, result ) ;
        if( wrapper != null) {
            wrapper.toCursor(result, fileName, cursor); }
        return result ;
    }

    private EvaluatorWrapper getWrapper( String guid, TMServiceResult result ) {
        EvaluatorWrapper wrapper ;
        synchronized(wrappers) { wrapper = wrappers.get(guid) ; }
        if( wrapper == null){
            result.statusCode = TMStatusCode.NO_EVALUATOR ;
            result.statusMessage = "Bad or stale GUID."; }
        return wrapper ;
    }
}