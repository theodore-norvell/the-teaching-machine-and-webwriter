package exceptions;

class Exception7 /*extends Throwable/**/ {
    Exception7() {}
    int i ;
}
public class ThrowCatch7 {
    static public void main() {
        int i = 0 ;
        try {
            f() ;
        }
        catch( Exception7 e ) {
            i = 1 ; }
        i = 2 ;
    }

    static void f() throws Exception7 {
        boolean x = true ;
        if( x ) throw new Exception7() ;
    }
}