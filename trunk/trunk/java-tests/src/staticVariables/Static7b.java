//! compile expect error matches 
//!   /.*line 15.*f must be a class variable \(it must be declared static\).*/

// Test error reporting when nonstatic field is used as static: FQN
package staticVariables;

class StaticFriend7b {
    int f = 40 ;
}

public class Static7b {
    static int f = 0 ;

    public static void main() {
        staticVariables.StaticFriend7b.f = staticVariables.StaticFriend7b.f + 1 ;
    }
}