//! Run. Expect output "100" endl.

//Test explicit super call.

package objectInitialization;


class Parent4 {
    Parent4( ) {
        System.out.println(100) ;
    }
    
    Parent4( int i ) {
        System.out.println(99) ;
    }
}
public class Initialization4 extends Parent4 {
    
    Initialization4() {
        super() ;
    }
    
    public static void main(/*String[] args*/) {
        Initialization4 p = new Initialization4() ;
    }
}
