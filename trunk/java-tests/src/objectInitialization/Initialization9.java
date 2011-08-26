//! Compile. Expect error matches /.*line 16/
//!   /.*No such constructor is accessable or applicable.*/

//Default initializer is missing.

package objectInitialization;


public class Initialization9  {
    
    public Initialization9() { }
    public Initialization9( int i ) { }
    public Initialization9( char c ) { }
    
    public static void main(/*String[] args*/) {
        Initialization9 p = new Initialization9(1.2) ;
    }
}
