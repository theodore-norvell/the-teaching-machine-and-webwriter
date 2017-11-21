package tm.gwt.server;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import tm.gwt.shared.TMServiceResult;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.TMStatusCode;

public class EvaluatorWrapperTest {
	
	public EvaluatorWrapper makeEvaluatorWrapper(String guid ) throws Throwable {
        TMServiceResult result = new TMServiceResult(guid) ;
		EvaluatorWrapper evaluatorWrapper = new EvaluatorWrapper( EvaluatorInterface.CPP_LANG, result); 
		assertEquals(TMStatusCode.READY_TO_COMPILE, result.statusCode );
		return evaluatorWrapper ;
	}

	@Test
	public void testEvaluatorWrapper() throws Throwable {
        String guid = UUID.randomUUID().toString();
	    EvaluatorWrapper wrapper = makeEvaluatorWrapper(guid) ;
	}

	@Test
	public void testLoadString()  throws Throwable {
        String guid = UUID.randomUUID().toString();
        EvaluatorWrapper wrapper = makeEvaluatorWrapper(guid) ;
        TMServiceResult result = new TMServiceResult(guid) ;
        String sourceCode = "int main() { }" ;
        wrapper.loadString(result, "newFile", sourceCode );
        if( result.statusCode != TMStatusCode.COMPILED ) {
            System.out.printf( result.statusMessage ) ;
            System.out.printf( result.attentionMessage ) ; 
        }
		assertEquals(TMStatusCode.COMPILED, result.statusCode );
	}

	@Test
	public void testLoadRemoteFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitializeTheState() {
		fail("Not yet implemented");
	}

	@Test
	public void testGo() {
		fail("Not yet implemented");
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
