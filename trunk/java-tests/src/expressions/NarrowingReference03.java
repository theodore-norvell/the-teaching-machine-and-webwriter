//! Compile. Run. Output = "0" endl
package expressions;

public class NarrowingReference03 {

    static public void main(){

        NarrowingReference03 p = new NarrowingReference03() ;
        NarrowingReferenceChild03 q  ;
        try {
            q = (NarrowingReferenceChild03)  p ;
            q.foo() ; }
        catch( ClassCastException e ) {
            System.out.println( 0 ) ;
        }
        
    }
}

class NarrowingReferenceChild03 extends NarrowingReference03 {
    void foo() { System.out.println(42) ; }
}