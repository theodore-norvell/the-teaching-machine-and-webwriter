//Check code in a "super" constructor call.
//Violates 6.5.6 (JLS 2.0)
//! compile expect error ~ /.*line 15.*TODO.*/

package staticContext;

class Parent8e0 {
    Parent8e0( int k ) { }
}

public class StaticContext8e0 extends Parent8e0 {
    int i =  17 ;
    
    StaticContext8e0() {
        super( i ) ; } // Error on this line
    
    public static void main(/*String[] args*/) {
        StaticContext8e0 p = new StaticContext8e0() ;
    }
}