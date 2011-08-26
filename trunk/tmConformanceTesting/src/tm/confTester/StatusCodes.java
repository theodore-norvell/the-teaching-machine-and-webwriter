/*
 * Created on Jun 16, 2005
 *
 */
package tm.confTester;

import tm.utilities.Assert;

/**
 * @author theo
 *
 */
public class StatusCodes {
    public static final int PASS = 0, MISMATCH = 1, FAIL = 2, ERROR = 3, NONTEST = 4, COUNT = 5 ;
    
    public static String toString( int kind ) {
        switch( kind ) {
        case PASS : return "PASS" ;
        case MISMATCH: return "MISMATCH" ;
        case FAIL : return "FAIL" ;
        case ERROR : return "ERROR" ;
        case NONTEST : return "NONTEST" ;
        default : Assert.check(false,  "Bad Status Code") ; return "" ;
        }
    }
}
