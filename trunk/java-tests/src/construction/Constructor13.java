//Test that default constructor is not built
//! compile expect error ~ /.*line 15.*No such constructor is accessable or applicable.*/
package construction;

/**
 * @author theo
 *
 */
public class Constructor13 {
    int i = 99  ;
    {
        // System.out.println("0") ;
    }
    
    Constructor13( int i ) { this() ; }
    
    public static void main(/*String[] args*/) {
        Constructor13 p = new Constructor13(1) ;
    }
}