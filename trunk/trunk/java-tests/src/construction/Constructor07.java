//Test the execution an explicit super call
//! run expect output = "16" endl "32" endl
package construction;

/**
 * @author theo
 *
 */

class Parent7 {
    protected int i ;
    
    Parent7(int m) {
        i = m+2 ;
        System.out.println( i ) ;
    }
}

public class Constructor07 extends Parent7 {
    Constructor07 (int k) {
        super(2*k) ;
        i = i * 2 ;
        System.out.println( i ) ;
    }
    
    public static void main(/*String[] args*/) {
        Constructor07 p = new Constructor07( 7 ) ;
    }
}
