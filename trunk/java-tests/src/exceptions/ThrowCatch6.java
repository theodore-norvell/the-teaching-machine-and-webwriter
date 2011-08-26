package exceptions;

class Exception6 /*extends Throwable/**/ {
    Exception6() {}
    int i ;
}

class Exception6a /*extends Throwable/**/ {
    Exception6a() {}
    int i ;
}
public class ThrowCatch6 {
    static public void main() {
        boolean x = true ;
        int i = 0 ;
        try {
            i = 1 ;
            try {
                i = 2 ;
                 if( x ) throw new Exception6() ;
                 i = -1 ;
             }
             catch( Exception6a e ) {
                 i = -1 ; }
             finally {
                 i = 3 ;} }
        catch( Exception6 e ) {
            i = 4 ; }
        finally {
            i = 5 ; }
        i = 6 ;
    }
}