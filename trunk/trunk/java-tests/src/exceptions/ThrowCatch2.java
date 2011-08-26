package exceptions;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

class Exception2 /*extends Throwable/**/ {
    Exception2() {}
    int i ;
}

public class ThrowCatch2 {
    static public void main() {
        boolean x = true ;
        int i = 0 ;
        try {
            if( x ) throw new Exception2() ;
            i = -1 ;
        }
        catch( Exception2 e ) {
            i = 1 ; }
        i = 2 ;
    }
}