//! compile expect error ~ /.*line 14.*No entity found matching id f.*/

// Test error reporting when static field is missing: simple name
package staticVariables;

/**
 * @author theo
 *
 */
public class Static5 {
    static int g = 0 ;

    public static void main() {
        f = f + 1 ;
        // System.out.println( f ) ;
    }
}
