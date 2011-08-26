//! run expect output = "1" endl "b" endl "a" endl

// Test static variables in the same class.
package staticVariables;

/**
 * @author theo
 *
 */
public class Static0 {
    private static int f = 0 ;
    private static char g = 'a' ;
    private static char h = 'b' ;

    public static void main() {
        f = f + 1 ;
        System.out.println( f ) ;
        char c = g ;
        g = h ;
        h = c ;
        System.out.println( g ) ;
        System.out.println( h ) ;
    }
}
