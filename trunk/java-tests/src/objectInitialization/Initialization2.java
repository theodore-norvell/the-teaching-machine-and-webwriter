//! Run. Expect output "98" endl.

// Initialization with implicit default constructor

package objectInitialization;

public class Initialization2 {
    int i = 98 ;
    

    public static void main(/*String[] args*/) {
        Initialization2 p = new Initialization2() ;
        System.out.println( p.i ) ;
    }
}
