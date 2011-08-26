// Check code in a static method
//! compile run expect output "17" endl
package staticContext;

/**
 * @author theo
 *
 */
public class StaticContext0p {
    static int i ;
    public static void main(/*String[] args*/) {
        { i = 17; }
        System.out.println(i) ;
    }
}
