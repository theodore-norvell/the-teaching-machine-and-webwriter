//Check code in a static field initializer
//Violates 15.12.3 (JLS 2.0)
//! compile expect error ~
//! /.*line 9.*Can't access Declaration of staticContext.StaticContext5e1.method in a static context.*/
package staticContext;

public class StaticContext5e1 {
    int method() { return 17 ; }
    static int i = method() ; // Error on this line
    
    StaticContext5e1() {
        int k ; }
    
    public static void main(/*String[] args*/) {
        StaticContext5e1 p = new StaticContext5e1() ;
    }
}