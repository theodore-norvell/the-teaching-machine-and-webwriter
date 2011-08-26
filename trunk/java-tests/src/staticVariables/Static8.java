//! run. expect output = "1" endl "42" endl.

// Test lookup of static field in a class in another package: Full import.
package staticVariables;

import staticVariables.anotherPackage.StaticFriend8 ;

public class Static8 {
    static int f = 0 ;

    public static void main() {
        f = f + 1 ;
        StaticFriend8.f = StaticFriend8.f + 1 ;
        staticVariables.anotherPackage.StaticFriend8.f
        = staticVariables.anotherPackage.StaticFriend8.f + 1 ;
        System.out.println( f ) ;
        System.out.println( StaticFriend8.f ) ;
    }
}