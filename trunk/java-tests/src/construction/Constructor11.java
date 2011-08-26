//Test the execution of a default constructor
//! run expect output = "0" endl "99" endl
package construction;

/**
* @author theo
*
*/
public class Constructor11 {
    int i = 99  ;
    {
        System.out.println("0") ;
    }
    
    public static void main(/*String[] args*/) {
        Constructor11 p = new Constructor11() ;
        System.out.println( p.i ) ;
    }
}