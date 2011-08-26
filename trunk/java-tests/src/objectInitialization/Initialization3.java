//! Run. Expect output "100" endl.

// Test implicit super call.

package objectInitialization;


class Parent3 {
    Parent3( ) {
        System.out.println(100) ;
    }
}
public class Initialization3 extends Parent3 {
    
    Initialization3() {
        
    }

    public static void main(/*String[] args*/) {
        Initialization3 p = new Initialization3() ;
    }
}
