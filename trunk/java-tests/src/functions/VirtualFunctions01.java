// Test the call of a function through the VFN
//! Run. Expect output = "42" endl ;
package functions;

public class VirtualFunctions01 {
    public static void main( /*String [] args*/ ) {
        VirtualFunctions01 p = new VirtualFunctions01() ;
        p.func() ;
    }
    
    void func() {
        System.out.println( 42 ) ;
    }
}
