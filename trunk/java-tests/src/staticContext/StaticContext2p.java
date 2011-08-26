//Check code in a constructor
//! compile run expect output "17" endl
package staticContext;

public class StaticContext2p {
    int i ;
    
    StaticContext2p() {
        int k ;
        i = 17 ; }
    
    public static void main(/*String[] args*/) {
        StaticContext2p p = new StaticContext2p() ;
        System.out.println( p.i ) ;
    }
}