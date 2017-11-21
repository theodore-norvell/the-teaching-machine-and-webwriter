package tm.gwt.server;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import tm.gwt.shared.TMServiceResult;
import tm.interfaces.LanguageCodes ;
import tm.interfaces.TMStatusCode;

public class EvaluatorWrapperTest {
	
	private EvaluatorWrapper makeEvaluatorWrapper(String guid ) throws Throwable {
        TMServiceResult result = new TMServiceResult(guid) ;
		EvaluatorWrapper evaluatorWrapper = new EvaluatorWrapper( LanguageCodes.CPP_LANG, result); 
		assertEquals(TMStatusCode.READY_TO_COMPILE, result.statusCode );
		return evaluatorWrapper ;
	}

    private EvaluatorWrapper loadString(String guid, String sourceCode) throws Throwable {
        EvaluatorWrapper wrapper = makeEvaluatorWrapper(guid) ;
        TMServiceResult result = new TMServiceResult(guid) ;
        wrapper.loadString(result, "newFile", sourceCode );
        if( result.statusCode != TMStatusCode.COMPILED ) {
            System.out.printf( result.statusMessage ) ;
            System.out.printf( result.attentionMessage ) ; 
        }
        assertEquals(TMStatusCode.COMPILED, result.statusCode );
        return wrapper ;
    }
    
    private EvaluatorWrapper loadAndInitialize(String guid, String sourceCode) throws Throwable {
        EvaluatorWrapper wrapper = loadString( guid, sourceCode ) ;
        TMServiceResult result = new TMServiceResult(guid) ;
        wrapper.initializeTheState( result ) ;
        if( result.statusCode != TMStatusCode.READY ) {
            System.out.printf( result.statusMessage ) ;
            System.out.printf( result.attentionMessage ) ; 
        }
        assertEquals(TMStatusCode.READY, result.statusCode );
        return wrapper ;
    }


	@Test
	public void testEvaluatorWrapper() throws Throwable {
        String guid = UUID.randomUUID().toString();
	    EvaluatorWrapper wrapper = makeEvaluatorWrapper(guid) ;
	}

	@Test
	public void testLoadString()  throws Throwable {
        String guid = UUID.randomUUID().toString();
        String sourceCode = "int main() { }" ;
        EvaluatorWrapper wrapper = loadString( guid, sourceCode ) ;
	}
	
	@Test
	public void testLoadRemoteFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitializeTheState() throws Throwable {
        String guid = UUID.randomUUID().toString();
        String sourceCode = "int main() { }" ;
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCode ) ;
	}

	@Test
	public void testGo() throws Throwable {
        String guid = UUID.randomUUID().toString();
        String sourceCode = "int main() { }" ;
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCode ) ;
        int status = TMStatusCode.READY ;
        for( int i = 0 ; i < 10 && status==TMStatusCode.READY ; ++i ) {
            TMServiceResult result = new TMServiceResult(guid) ;
            wrapper.go( result, "f" ) ;
            status = result.statusCode ;
        }
        assertEquals(TMStatusCode.EXECUTION_COMPLETE, status );
	}

	@Test
	public void testGoBack() {
		fail("Not yet implemented");
	}

	@Test
	public void testToCursor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetResult() {
		fail("Not yet implemented");
	}

}
