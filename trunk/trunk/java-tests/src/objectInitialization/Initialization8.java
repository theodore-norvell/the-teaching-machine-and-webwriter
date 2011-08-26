//! Compile. Expect error matches /.*line 18.*/
//!    /No such constructor is accessable or applicable.*/

// Default initializer is missing.

package objectInitialization;

public class Initialization8 {
    int i = 98 ;
    
    Initialization8(int i) {
    }
    
    Initialization8( char c) {
    }
    
    public static void main(/*String[] args*/) {
        Initialization8 p = new Initialization8() ;
    }
}
