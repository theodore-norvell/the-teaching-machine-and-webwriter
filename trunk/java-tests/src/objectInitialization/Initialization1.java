//! Run. Expect output "99" endl.

// Initialization with default constructor.

package objectInitialization;

public class Initialization1 {
    int i = 98 ;
    
    Initialization1() {
        i = i+1 ;
    }

    public static void main(/*String[] args*/) {
        Initialization1 p = new Initialization1() ;
        System.out.println( p.i ) ;
    }
}
