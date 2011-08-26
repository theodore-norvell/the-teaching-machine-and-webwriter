//! Run. Expect error matches /.*TBD.*/.
package runtimeErrorReporting;

public class NullPointer0 {
    
    int f ;
    
    public static void main( ) {
        NullPointer0 p ;
        p = null ;
        p.f = 0 ;
    }
}
