//! Compile. Expect error matches /.*line 15/
//!   /.*No such constructor is accessable or applicable.*/

//Default initializer is missing.

package objectInitialization;

class Parent9b {
    public Parent9b( int i ) { }
    public Parent9b( char c ) { }
}

public class Initialization9b extends Parent9b {
    
    public Initialization9b() { super( 1.2 ) ; }
    
    public static void main(/*String[] args*/) {
        Initialization9b p = new Initialization9b() ;
    }
}
