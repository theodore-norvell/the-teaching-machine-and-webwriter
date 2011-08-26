package exceptions;

class Exception3 /*extends Throwable/**/ {
    Exception3() {}
    int i ;
}

class Exception3a /*extends Throwable/**/ {
    Exception3a() {}
    int i ;
}
public class ThrowCatch3 {
    static public void main() {
        boolean x = true ;
        int i = 0 ;
        try {
            if( x ) throw new Exception3() ;
            i = -1 ;
        }
        catch( Exception3a e ) {
            i = -1 ; }
        catch( Exception3 e ) {
            i = 1 ; }
        i = 2 ;
    }
}