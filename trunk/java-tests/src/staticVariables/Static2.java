//! run. expect output = "1" endl "42" endl.

// Test lookup of static variables from another class in same file.
package staticVariables;

class StaticFriend2 {
    static int f = 40 ;
}

public class Static2 {
    static int f = 0 ;

    public static void main() {
        f = f + 1 ;
        StaticFriend2.f = StaticFriend2.f + 1 ;
        staticVariables.StaticFriend2.f = staticVariables.StaticFriend2.f + 1 ;
        System.out.println( f ) ;
        System.out.println( StaticFriend2.f ) ;
    }
}