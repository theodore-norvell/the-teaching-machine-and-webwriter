//Check code in a static method.
// Violates 15.12.3 (JLS 2.0)
//! compile expect error ~
//! /.*line 16.*Can't access Declaration of staticContext.StaticContext0e1.method/
//! / in a static context.*/

package staticContext;


public class StaticContext0e1 {
    int i ;
     
    void method() { }
    
    public static void main(/*String[] args*/) {
        {  method() ; } // Error on this line
    }
}