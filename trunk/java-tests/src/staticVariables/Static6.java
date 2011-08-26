//! compile expect error matches /.*line 15.*Can't access f.*/
package staticVariables;

//Test error reporting when static field is missing: qualified name

class StaticFriend6 {
    private static int f = 40 ;
}

public class Static6 {
    static int f = 0 ;

    public static void main() {
        f = f + 1 ;
        StaticFriend6.f = StaticFriend6.f + 1 ;
    }
}