//! run. expect output = "1" endl "41" endl.

//Test lookup of static field in a class in another package: No import.
package staticVariables;

// No import this time

public class Static8a {
    static int f = 0 ;

    public static void main() {
        f = f + 1 ;
        staticVariables.anotherPackage.StaticFriend8.f
        = staticVariables.anotherPackage.StaticFriend8.f + 1 ;
        System.out.println( f ) ;
        System.out.println( staticVariables.anotherPackage.StaticFriend8.f ) ;
    }
}