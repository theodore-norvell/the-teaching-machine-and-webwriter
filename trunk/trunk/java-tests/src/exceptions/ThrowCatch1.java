package exceptions;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ThrowCatch1 {
    private static class Exception /*extends Throwable*/ {
        Exception() { }
        int k ;
    }
    static public void main() {
        int i = 0 ;
        boolean x = true ;
        try {
            if(x ) throw new Exception() ;
            i = -1 ;
        }
        catch( Exception e ) {
            i = 1 ; }
        i = 2 ;
    }
}