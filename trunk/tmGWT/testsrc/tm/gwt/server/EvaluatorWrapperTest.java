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
        TMServiceStatusReporter statusReporter = new TMServiceStatusReporter( result ) ;
		statusReporter.setResult(result);
		EvaluatorWrapper evaluatorWrapper = new EvaluatorWrapper( EvaluatorInterface.CPP_LANG, statusReporter); 
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
        wrapper.loadString(result, "newFile", "// This is a comment");
		assertEquals(TMStatusCode.COMPILED, result.statusCode );
		System.out.print(result.statusMessage);
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
