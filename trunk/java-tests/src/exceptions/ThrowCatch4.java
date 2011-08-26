package exceptions;

class Exception4 /*extends Throwable/**/ {
    Exception4() {}
    int i ;
}

class Exception4a /*extends Throwable/**/ {
    Exception4a() {}
    int i ;
}
public class ThrowCatch4 {
    static public void main() {
        boolean x = true ;
        int i = 0 ;
        try {
            if( x ) throw new Exception4() ;
            i = -1 ;
        }
        catch( Exception4 e ) {
            i = 1 ; }
        catch( Exception4a e ) {
            i = -1 ; }
        i = 2 ;
    }
}