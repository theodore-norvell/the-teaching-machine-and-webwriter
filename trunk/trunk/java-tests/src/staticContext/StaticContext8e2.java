//Check code in a "super" constructor call.
//Violates 15.8.3 (JLS 2.0)
//! compile expect error ~ /.*line 15.*TODO.*/

package staticContext;

class Parent8e2 {
    Parent8e2( int k ) { }
}

public class StaticContext8e2 extends Parent8e2 {
    int i = 17 ;
    
    StaticContext8e2() {
        super( this.i ) ; } // Error on this line
    
    public static void main(/*String[] args*/) {
        StaticContext8e2 p = new StaticContext8e2() ;
    }
}