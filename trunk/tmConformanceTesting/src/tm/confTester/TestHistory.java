/*
 * Created on Apr 10, 2005
 *
 */
package tm.confTester;

import java.io.PrintStream;
import java.util.* ;

/**
 * @author theo
 *
 */
public class TestHistory {
    private ConformanceTester frame ;
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private int[] count = new int[StatusCodes.COUNT] ;
    
    synchronized void reset() {
        for( int i=0 ; i<StatusCodes.COUNT ; ++i ) count[i] = 0 ;
        map = new HashMap<String, Integer>() ;
        frame.updateStatus() ; }
    
    TestHistory( ConformanceTester frame ) {
        this.frame = frame ; }

    /**
     * @param kind
     * @return Returns the errorCount.
     */
    int getCount(int kind ) {
        return count[kind];
    }
    /**
     */
    synchronized void record( String fileName, int kind ) {
        if( map.containsKey( fileName ) ) {
            int k = ((Integer) map.get( fileName )).intValue() ;
            count[k] -= 1 ;
            map.remove( fileName ) ; }
        map.put( fileName, new Integer(kind) ) ;
        count[ kind ] += 1 ;
    }
    
    void dump( PrintStream out) {
        out.println("Passes: " + count[StatusCodes.PASS]) ;
        out.println("Mismatches: " + count[StatusCodes.MISMATCH]) ;
        out.println("Errors: " + count[StatusCodes.ERROR]) ;
        out.println("Fails: " + count[StatusCodes.FAIL]) ;
        out.println("Errors: " + count[StatusCodes.ERROR]) ;
        out.println("Nontests: " + count[StatusCodes.NONTEST]) ;
        for(String fileName : map.keySet()) {
            int kind = map.get(fileName) ;
            out.println(StatusCodes.toString(kind) +" " + fileName) ;
        }
    }
}