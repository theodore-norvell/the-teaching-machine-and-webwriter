//! Run. Expect output "".

//Test explicit super call.

package objectInitialization;


class Parent5a {
    Parent5a( ) {
    }
    
    Parent5a( int i ) {
    }
    
    Parent5a( char i ) {
    }
}
public class Initialization5a extends Parent5a {
    
    Initialization5a() {
        super('a') ;
    }
    
    public static void main(/*String[] args*/) {
        Initialization5a p = new Initialization5a() ;
    }
}