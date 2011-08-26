//! Compile. Run; expect output "42" endl.
package expressions;

public class NarrowingReference02 {

    static public void main(){

        NarrowingReference02 p = new NarrowingReferenceChild02() ;
        NarrowingReferenceChild02 q  ;
        q = (NarrowingReferenceChild02)  p ;
        q.foo() ;
        
    }
}

class NarrowingReferenceChild02 extends NarrowingReference02 {
    void foo() { System.out.println(42) ; }
}