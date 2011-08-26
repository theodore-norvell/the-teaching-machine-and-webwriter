//Test the execution an explicit super call with an inner superclass
//! run expect output = "42" endl
package construction;

/**
 * @author theo
 *
 */

class Constructor09 {
    int z ;
    Constructor09( int j ) { z = j ; }
    
    class Parent9 {
        int i ;
        Parent9(int m) {
            i = m+z ;
        }
    }
    
    public class Inner9 extends Constructor09.Parent9 {
        Inner9 () {
            super(4) ;
            System.out.println( i ) ;
        }
    }
    
    public static void main(/*String[] args*/) {
        Constructor09 outer = new Constructor09(38) ;
        Inner9 p = outer.new Inner9( ) ;
    }
}