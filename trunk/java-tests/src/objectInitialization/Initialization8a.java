//! Compile. Expect error matches /.*line 14/
//!   /.*No such constructor is accessable or applicable.*/

//Default initializer is missing.

package objectInitialization;

class Parent8a {
    Parent8a( int i ) { }
    Parent8a( char c ) { }
}
public class Initialization8a extends Parent8a {
    
    Initialization8a() {
    }
    
    public static void main(/*String[] args*/) {
        Initialization8a p = new Initialization8a() ;
    }
}
