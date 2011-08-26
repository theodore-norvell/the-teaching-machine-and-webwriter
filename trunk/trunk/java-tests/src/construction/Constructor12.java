//Test that default constructor is not built
//! compile expect error ~ /.*line 18.*No such constructor is accessable or applicable.*/
package construction;

/**
 * @author theo
 *
 */
public class Constructor12 {
    int i = 99  ;
    {
        // System.out.println("0") ;
    }
    
    Constructor12( int i ) { }
    
    public static void main(/*String[] args*/) {
        Constructor12 p = new Constructor12() ;
        // System.out.println( p.i ) ;
    }
}