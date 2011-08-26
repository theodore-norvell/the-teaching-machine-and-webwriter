//! run expect output = "4" endl
// Test use of partially and full qualified names to access static variables.
package staticVariables;

/**
 * @author theo
 *
 */
public class Static1 {
    static int f = 2 ;

    public static void main() {
        Static1.f = Static1.f + 1 ;
        staticVariables.Static1.f = staticVariables.Static1.f + 1 ;
        System.out.println( f ) ;
    }
}