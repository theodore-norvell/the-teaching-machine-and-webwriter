package jsnoopytest;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

import jsnoopy.ReplayTestSuite;
import junit.framework.* ;

public class TestSuiteForTestMain extends ReplayTestSuite {

    public static Test suite() {
        TestSuite suite= new TestSuite("Test Suite for Testing jsnoopytest.TestMain");
        ReplayTestSuite thisTest = new TestSuiteForTestMain() ;
        thisTest.addFile("baseline.jst");
        thisTest.addFile("baseline1.jst");
        thisTest.addFile("nofile.jst");
        thisTest.addFile("baseline.jst");
        suite.addTest( thisTest );
        return suite ; }

    public void setUpForSuite() {
        TestMain.main( new String[0] ) ;
    }
}