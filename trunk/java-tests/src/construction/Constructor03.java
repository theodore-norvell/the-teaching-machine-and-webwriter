//Test the execution of a constructor with initialization
//! run expect output = "3" endl "8" endl
package construction;

/**
 * @author theo
 *
 */
public class Constructor03 {
    int i = 3 ;
    int j = 5 ;
    int k = sum() ;
    {
        System.out.println(i) ;
    }
    
    private int sum() { return i+j ; }
    
    Constructor03 () {
        System.out.println( k ) ;
    }
    
    public static void main( /*String[] args*/ ) {
        Constructor03 p = new Constructor03() ;
    }
}
