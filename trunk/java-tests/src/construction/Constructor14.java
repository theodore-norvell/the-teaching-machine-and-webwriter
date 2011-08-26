//Test that default constructor is not built
//! compile expect error ~ /.*line 20.*No such constructor is accessable or applicable.*/
package construction;

/**
 * @author theo
 *
 */

class Parent14 {
    Parent14(char c) {}
}

public class Constructor14 extends Parent14{
    int i = 99  ;
    {
        // System.out.println("0") ;
    }
    
    Constructor14( int i ) { super() ; }
    
    public static void main(/*String[] args*/) {
        Constructor14 p = new Constructor14(1) ;
    }
}