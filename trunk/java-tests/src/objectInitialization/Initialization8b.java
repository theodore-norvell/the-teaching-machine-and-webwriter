//! Compile. Expect error matches /.*line 12/
//!   /.*No such constructor is accessable or applicable.*/

//Default initializer is missing.

package objectInitialization;

class Parent8b {
    Parent8b( int i ) { }
    Parent8b( char c ) { }
}
public class Initialization8b extends Parent8b {
       
    public static void main(/*String[] args*/) {
        Initialization8b p = new Initialization8b() ;
    }
}
