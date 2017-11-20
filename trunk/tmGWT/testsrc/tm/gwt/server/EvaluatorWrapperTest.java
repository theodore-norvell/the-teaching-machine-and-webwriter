package tm.gwt.server;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import tm.gwt.shared.TMServiceResult;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.TMStatusCode;

public class EvaluatorWrapperTest {
	
	EvaluatorWrapper evaluatorWrapper;
	TMServiceStatusReporter statusReporter;
	TMServiceResult result ;
	String guid ;

	@Before
	public void setUp() throws Throwable {
        guid = UUID.randomUUID().toString();
        result = new TMServiceResult(guid) ;
		statusReporter = new TMServiceStatusReporter( result ) ;
		statusReporter.setResult(result);
		evaluatorWrapper = new EvaluatorWrapper( EvaluatorInterface.CPP_LANG, statusReporter); 
	}

	@Test
	public void testEvaluatorWrapper() throws Throwable {
		assertEquals(TMStatusCode.READY_TO_COMPILE, statusReporter.getStatusCode());
		System.out.print(result.statusMessage);
	}

	@Test
	public void testLoadString() {
		assertEquals(TMStatusCode.READY_TO_COMPILE, statusReporter.getStatusCode());
		result = new TMServiceResult(guid) ;
		evaluatorWrapper.loadString(result, "newFile", "// This is a comment");
		assertEquals(TMStatusCode.COMPILED, statusReporter.getStatusCode());
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
