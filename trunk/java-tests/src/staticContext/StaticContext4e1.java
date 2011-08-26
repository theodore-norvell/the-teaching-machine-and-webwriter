//Check code in a static initialization block
//Violates 15.12.3 (JLS 2.0)
//! compile expect error ~ /.*line 9.*Can't access Declaration of staticContext.StaticContext4e1.method in a static context.*/
package staticContext;

public class StaticContext4e1 {
    int i ;
    static { 
        {method() ;} // Error on this line
    }
    void method() { }
    
    StaticContext4e1() {
        int k ; }
    
    public static void main(/*String[] args*/) {
        StaticContext4e1 p = new StaticContext4e1() ;
    }
}