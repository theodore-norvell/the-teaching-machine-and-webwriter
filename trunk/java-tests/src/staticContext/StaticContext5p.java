//Check code in a static field initializer
//! run expect output "17" endl
package staticContext;

public class StaticContext5p {
    static int j =  17 ;
    static int i = j ;
    
    StaticContext5p() {
        int k ; }
    
    public static void main(/*String[] args*/) {
        StaticContext5p p = new StaticContext5p() ;
        System.out.println( p.i ) ;
    }
}