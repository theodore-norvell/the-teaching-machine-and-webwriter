package exceptions;

class Exception5 /*extends Throwable/**/ {
    Exception5() {}
    int i ;
}

class Exception5a /*extends Throwable/**/ {
    Exception5a() {}
    int i ;
}
public class ThrowCatch5 {
    static public void main() {
        boolean x = true ;
        int i = 0 ;
        try {
            i = 1 ;
            try {
                i = 2 ;
                 if( x ) throw new Exception5() ;
                 i = -1 ;
             }
             catch( Exception5a e ) {
                 i = -1 ; } }
        catch( Exception5 e ) {
            i = 3 ; }
        i = 4 ;
    }
}