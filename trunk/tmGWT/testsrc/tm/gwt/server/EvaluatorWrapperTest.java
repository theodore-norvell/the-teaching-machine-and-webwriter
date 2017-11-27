package tm.gwt.server;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before ;
import org.junit.Test;

import tm.gwt.shared.TMServiceResult;
import tm.gwt.shared.state.MirrorState ;
import tm.interfaces.LanguageCodes ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.TMStatusCode;
import tm.utilities.Debug ;

public class EvaluatorWrapperTest {
    

    String sourceCodeFortyTwo = "#include <iostream>\n"
                              + "int main() {\n"
                              + "    int x = 41 ;\n"
                              + "    x = x + 1 ;\n"
                              + "    cout<<x ;\n"
                              + "    return 0 ; }\n" ;
    
    @Before
    public void before() {
        Debug.getInstance().turnOffAll();
    }
	
	private EvaluatorWrapper makeEvaluatorWrapper(String guid ) throws Throwable {
        TMServiceResult result = new TMServiceResult(guid) ;
		EvaluatorWrapper evaluatorWrapper = new EvaluatorWrapper( LanguageCodes.CPP_LANG, result); 
		assertEquals(TMStatusCode.READY_TO_COMPILE, result.statusCode );
		return evaluatorWrapper ;
	}

    private EvaluatorWrapper loadString(String guid, String sourceCode, int expectedStatus, MirrorState clientSideMirrorState) throws Throwable {
        EvaluatorWrapper wrapper = makeEvaluatorWrapper(guid) ;
        TMServiceResult result = new TMServiceResult(guid) ;
        wrapper.loadString(result, "newFile", sourceCode );
        if( result.statusCode != expectedStatus ) {
            System.out.println( result.statusMessage ) ;
            System.out.println( result.attentionMessage ) ; 
        }
        assertEquals(expectedStatus, result.statusCode );
        clientSideMirrorState.update( result.resultState ) ;
        return wrapper ;
    }
    
    private EvaluatorWrapper loadAndInitialize(String guid, String sourceCode, int expectedStatus, MirrorState clientSideMirrorState) throws Throwable {
        EvaluatorWrapper wrapper = loadString( guid, sourceCode, TMStatusCode.COMPILED, clientSideMirrorState ) ;
        TMServiceResult result = new TMServiceResult(guid) ;
        wrapper.initializeTheState( result ) ;
        if( result.statusCode != expectedStatus ) {
            System.out.println( result.statusMessage ) ;
            System.out.println( result.attentionMessage ) ; 
        }
        assertEquals( expectedStatus, result.statusCode );
        clientSideMirrorState.update( result.resultState ) ;
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
        MirrorState clientSideMirrorState = new MirrorState() ;
        EvaluatorWrapper wrapper = loadString( guid, sourceCodeFortyTwo, TMStatusCode.COMPILED, clientSideMirrorState ) ;
	}
	
	@Test
	public void testLoadRemoteFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitializeTheState() throws Throwable {
        String guid = UUID.randomUUID().toString();
        MirrorState clientSideMirrorState = new MirrorState() ;
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCodeFortyTwo, TMStatusCode.READY, clientSideMirrorState ) ;
	}

	@Test
	public void testGo() throws Throwable {
        String guid = UUID.randomUUID().toString();
        MirrorState clientSideMirrorState = new MirrorState() ;
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCodeFortyTwo, TMStatusCode.READY, clientSideMirrorState  ) ;
        int status = TMStatusCode.READY ;
        TMServiceResult result = null ;
        for( int i = 0 ; i < 100 && status==TMStatusCode.READY ; ++i ) {
            result = new TMServiceResult(guid) ;
            wrapper.go( result, "f" ) ;
            status = result.statusCode ;
            clientSideMirrorState.update(  result.resultState );
        }
        assertEquals(TMStatusCode.EXECUTION_COMPLETE, status );
        // TODO. Next line fails because of a bug in the update method.
        assertEquals( 1, result.resultState.getNumConsoleLines() ) ;
        assertEquals( "42", result.resultState.getConsoleLine( 0 ) )  ;
        assertEquals( 1, clientSideMirrorState.getNumConsoleLines() ) ;
        assertEquals( "42", clientSideMirrorState.getConsoleLine( 0 ) )  ;
	}

    @Test
    public void testDatumCopying0() throws Throwable {
        // A very minimal test that dataums are copied.
        String guid = UUID.randomUUID().toString();
        MirrorState clientSideMirrorState = new MirrorState() ;
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCodeFortyTwo, TMStatusCode.READY, clientSideMirrorState  ) ;
        int status = TMStatusCode.READY ;
        TMServiceResult result = null ;
        // Go forward 9 steps after initialization.
        for( int i = 0 ; i < 9 && status==TMStatusCode.READY ; ++i ) {
            result = new TMServiceResult(guid) ;
            wrapper.go( result, "f" ) ;
            status = result.statusCode ;
            clientSideMirrorState.update( result.resultState ) ;
        }
        assertEquals(TMStatusCode.READY, status );
        RegionInterface stackRegion = clientSideMirrorState.getStackRegion() ;
        assertEquals( 1, stackRegion.getNumChildren() ) ;
        assertEquals( "42", stackRegion.getChildAt( 0 ).getValueString() )  ;
        assertEquals( "x", stackRegion.getChildAt( 0 ).getName() )  ;
        assertEquals( "int", stackRegion.getChildAt( 0 ).getTypeString() )  ;
        assertEquals( 4, stackRegion.getChildAt( 0 ).getNumBytes() )  ;
        assertEquals( 42, stackRegion.getChildAt( 0 ).getByte( 0 ) )  ;
        assertEquals( 0, stackRegion.getChildAt( 0 ).getByte( 1 ) )  ;
        assertEquals( 0, stackRegion.getChildAt( 0 ).getByte( 2 ) )  ;
        assertEquals( 0, stackRegion.getChildAt( 0 ).getByte( 3 ) )  ;
    }

	@Test
	public void testGoBack() throws Throwable {
        String guid = UUID.randomUUID().toString();
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCodeFortyTwo, TMStatusCode.READY  ) ;
        int status = TMStatusCode.READY ;
        TMServiceResult result = null ;
        result = new TMServiceResult(guid) ;
        if(status == TMStatusCode.READY){
        	wrapper.go(result, "f");
        	status = result.statusCode ;
        }
        
        result = new TMServiceResult(guid) ;
        wrapper.goBack(result);
        assertEquals(TMStatusCode.READY, status );
        
        for( int i = 0 ; i < 100 && status==TMStatusCode.READY ; ++i ) {
            result = new TMServiceResult(guid) ;
            wrapper.go( result, "f" ) ;
            status = result.statusCode ;
        }
        
        
        for( int i = 0 ; i < 100 ; ++i ) {
            result = new TMServiceResult(guid) ;
            wrapper.goBack( result) ;
            status = result.statusCode ;
        }
        assertEquals(TMStatusCode.EXECUTION_COMPLETE, status );
        assertEquals( 0, result.resultState.getNumConsoleLines() ) ;
	}

	@Test
	public void testToCursor() throws Throwable  {
        String guid = UUID.randomUUID().toString();
        EvaluatorWrapper wrapper = loadAndInitialize( guid, sourceCodeFortyTwo, TMStatusCode.READY  ) ;
        int status = TMStatusCode.READY ;
        TMServiceResult result = null ;
        result = new TMServiceResult(guid) ;
        
        wrapper.toCursor(result,"sourceCodeFortyTwo", 2 );
        int expectedCursorLine = 2;
        assertEquals(expectedCursorLine, result.resultState.getCodeFocus().getLineNumber() );
        
	}

}
