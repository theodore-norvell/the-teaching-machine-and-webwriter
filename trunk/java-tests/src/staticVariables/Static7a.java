//! compile expect error matches
//!   /.*f must be a class variable \(it must be declared static\).*/

// Test error reporting when a nonsttic field is used as if static: PQN

package staticVariables;

class StaticFriend7a {
    int f = 40 ;
}

public class Static7a {
    static int f = 0 ;

    public static void main() {
        StaticFriend7a.f = StaticFriend7a.f + 1 ;
    }
}