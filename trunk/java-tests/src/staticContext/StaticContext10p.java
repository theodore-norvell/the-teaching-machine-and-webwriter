//Check code in a static method
//! compile run expect output = "17" endl

package staticContext;

/**
 * @author theo
 *
 */
public class StaticContext10p {
    static StaticContext10p p = new StaticContext10p() ;
    
    StaticContext10p() { }
    
    int f = 16 ;
    
    public static void main(/*String[] args*/) {
        p.f = p.f + 1 ;
        System.out.println(p.f) ;
    }
}