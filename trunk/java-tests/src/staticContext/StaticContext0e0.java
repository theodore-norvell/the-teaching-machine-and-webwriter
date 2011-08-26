//Check code in a static method. Violates 6.5.6 (JLS 2.0)
//! compile expect error ~ 
//!  /.*line 11.*Can't refer to an instance variable in a static context.*/

package staticContext;


public class StaticContext0e0 {
    int i ;
    public static void main(/*String[] args*/) {
        { i = 17; } // Error on this line
    }
}