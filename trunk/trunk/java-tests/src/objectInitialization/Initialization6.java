//! Run. Expect output "99" endl.

//Test explicit super call.

package objectInitialization;

class GrandParent6 {
    GrandParent6() {
    }
    
    GrandParent6( char i ) {
        System.out.println(98) ;
    }
}
class Parent6 extends GrandParent6 {
    Parent6( ) {
        System.out.println(100) ;
    }
    
    Parent6( int i ) {
        System.out.println(99) ;
    }
}
public class Initialization6 extends Parent6 {
    
    Initialization6() {
        super('a') ;
    }
    
    public static void main(/*String[] args*/) {
        Initialization6 p = new Initialization6() ;
    }
}