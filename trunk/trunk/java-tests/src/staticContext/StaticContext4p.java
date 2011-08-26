//Check code in a static initialization block
//! compile run expect output "17" endl
package staticContext;

public class StaticContext4p {
    static int i ;
    static { 
        i = 17 ;
    }
    
    StaticContext4p() {
        int k ; }
    
    public static void main(/*String[] args*/) {
        StaticContext4p p = new StaticContext4p() ;
        System.out.println( p.i ) ;
    }
}