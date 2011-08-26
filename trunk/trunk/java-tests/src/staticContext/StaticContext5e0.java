//Check code in a static field initializer
//Violates 6.5.6 (JLS 2.0)
//! compile expect error ~
//! /.*line 9.*Can't refer to an instance variable in a static context.*/
package staticContext;

public class StaticContext5e0 {
    int j =  17 ;
    static int i = j ; // Error on this line
    
    StaticContext5e0() {
        int k ; }
    
    public static void main(/*String[] args*/) {
        StaticContext5e0 p = new StaticContext5e0() ;
    }
}