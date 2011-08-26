//! run expect output = "4" endl
//Test use of partial and fully qualified names in presence of import

package staticVariables;

import staticVariables.Static1b ;

public class Static1b {
    static int f = 2 ;

    public static void main() {
        Static1b.f = Static1b.f + 1 ;
        staticVariables.Static1b.f = staticVariables.Static1b.f + 1 ;
        System.out.println( f ) ;
    }
}