// Test the execution of a simple constructor
//! run expect output = "1" endl
package construction;

/**
 * @author theo
 *
 */
public class Constructor01 {
    Constructor01 () {
        int i = 0 ;
        System.out.println( 1 ) ;
    }

    public static void main(/*String[] args*/) {
        Constructor01 p = new Constructor01() ;
    }
}
