package jsnoopy;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

import java.io.* ;
import java.util.* ;
import junit.framework.* ;

import jsnoopy.parser.Parser;

abstract public class ReplayTestSuite implements Test {

    private Vector /* of String */ testFileNames = new Vector();

    public void addFile( String fileName ) {
        testFileNames.addElement( fileName ); }

    public int countTestCases() {
        return 1 ; }

    public void setUpForSuite() throws Exception {}

    public void setUpForTest()  throws Exception {}

    public void tearDownForTest()  throws Exception {}

    public void tearDownForSuite()  throws Exception {}

    public void run(TestResult result) {
        try {

            setUpForSuite() ;
            try {
                runEachTest( result ) ; }
            finally {
                tearDownForSuite() ; } }

        catch (AssertionFailedError e) {
            result.addFailure(this, e);
        }
        catch (ThreadDeath e) { // don't catch ThreadDeath by accident
            throw e;
        }
        catch (Throwable e) {
            result.addError(this, e);
        }
    }

    private void runEachTest( TestResult result ) {
        for( int i=0, sz=testFileNames.size() ; i<sz ; ++i ) {
            String fileName = (String) testFileNames.elementAt(i) ;
            runOneTestFile( fileName, result ) ;}
    }

    private void runOneTestFile( final String fileName, final TestResult result ) {
        result.startTest( this );

        try {
            // The actual testing is done by the AWT's event
            // dispatch thread.
            java.awt.EventQueue.invokeAndWait( new Runnable() {
                public void run() {
                    try {
                        setUpForTest() ;
                        try {
                            runOneTestFileInner( fileName ) ; }
                        finally {
                            tearDownForTest() ; }
                    }
                    catch (AssertionFailedError e) {
                        result.addFailure(ReplayTestSuite.this, e);
                    }
                    catch (JSnoopyException e) {
                        Throwable t = e.getWrappedThrowable() ;
                        result.addError( ReplayTestSuite.this, t==null ? e : t ) ; }
                    catch (ThreadDeath e) { // don't catch ThreadDeath by accident
                        throw e;
                    }
                    catch (Throwable e) {
                        result.addError(ReplayTestSuite.this, e); } } } ) ;
        }
        catch( InterruptedException e ) {
            result.addError( this, e ) ; }
        catch( java.lang.reflect.InvocationTargetException e ) {
            result.addError( this, e ) ; }
        finally {
            result.endTest( this ) ; }
    }

    private void runOneTestFileInner( String fileName ) throws Throwable {
        File file = new File( fileName ) ;
        InputStream is ;
        is = new FileInputStream(file) ;
        Trace oldTrace = Parser.parseTrace( is ) ;

        Manager traceMan = Manager.getManager() ;
        traceMan.startRecording();
        Trace newTrace ;
        try {
            oldTrace.run( traceMan, 0 ) ; }
        finally {
            newTrace = traceMan.stopRecording() ; }

        int firstDiff = newTrace.compare( oldTrace ) ;
        boolean equal = firstDiff == -1 ;

        if( !equal ) {

            File dumpFile = new File( "newtrace.jst" ) ;
            PrintStream ps = new PrintStream( new FileOutputStream( dumpFile ) ) ;
            newTrace.printSelf( ps ) ;
            System.out.println( "First Diff at event "+firstDiff );
            if( oldTrace.size() > firstDiff ) {
                System.out.println( "Old: "+oldTrace.get(firstDiff) ) ; }
            else {
                System.out.println("Old event missing"); }
            if( newTrace.size() > firstDiff ) {
                System.out.println( "New: "+newTrace.get(firstDiff) ) ; }
            else {
                System.out.println("New event missing"); }

            AssertionFailedError failure = new AssertionFailedError(
                    "Failed trace comparison for file "+fileName ) ;
            throw failure ; }
    }
}