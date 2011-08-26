//Check code in a "super" constructor call.
//Violates 15.12.3 (JLS 2.0)
//! compile expect error ~ /.*line 15.*TODO.*/

package staticContext;

class Parent8e1 {
    Parent8e1( int k ) { }
}

public class StaticContext8e1 extends Parent8e1 {
    int method() { return 17 ; }
    
    StaticContext8e1() {
        super( method() ) ; } // Error on this line
    
    public static void main(/*String[] args*/) {
        StaticContext8e1 p = new StaticContext8e1() ;
    }
}