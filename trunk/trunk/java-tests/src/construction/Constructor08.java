//Test the execution an explicit super call with an inner superclass
//! run expect output = "45" endl
package construction;

/**
* @author theo
*
*/

class Outer8 {
    int z ;
    Outer8( int j ) { z = j ; }
    
    class Parent8 {
        int i ;
        Parent8(int m) {
            i = m+z ;
        }
    }
}

public class Constructor08 extends Outer8.Parent8 {
    Constructor08 (Outer8 outer) {
        outer.super(3) ;
        System.out.println( i ) ;
    }
    
    public static void main(/*String[] args*/) {
        Outer8 outer = new Outer8(42) ;
        Constructor08 p = new Constructor08( outer ) ;
    }
}