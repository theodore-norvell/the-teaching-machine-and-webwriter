package tm.gwt.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tm.gwt.shared.TMServiceResult;
import tm.interfaces.TMStatusCode;

public class EvaluatorWrapperTest {
	
	EvaluatorWrapper evaluatorWrapper;
	TMServiceStatusReporter statusReporter;
	TMServiceResult result ;

	@Before
	public void setUp() throws Exception {
		statusReporter.setResult(result);
	}

	@Test
	public void testEvaluatorWrapper() throws Throwable {
		evaluatorWrapper = new EvaluatorWrapper( 2, statusReporter);
		assertEquals(TMStatusCode.READY_TO_COMPILE, statusReporter.getStatusCode());
		System.out.print(result.statusMessage);
	}

	@Test
	public void testLoadString() {
		result = new TMServiceResult() ;
		evaluatorWrapper.loadString(result, "newFile", "// This is a comment");
		
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
