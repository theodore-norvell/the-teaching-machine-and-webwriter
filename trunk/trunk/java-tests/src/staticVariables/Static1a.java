//! run expect output = "4" endl 
// Test use of partial and fully qualified names in presence of demand import
package staticVariables;


import staticVariables.* ;

public class Static1a {
    static int f = 2 ;

    public static void main() {
        Static1a.f = Static1a.f + 1 ;
        staticVariables.Static1a.f = staticVariables.Static1a.f + 1 ;
        System.out.println( f ) ;
    }
}