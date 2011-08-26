//! run expect output = "0" endl "0" endl "43" endl

//Test lookup of static variables from another class in same file
// using field access expression.

package staticVariables;

/**
 * @author theo
 *
 */
class StaticFriend3 {
    static int f = 40 ;
    StaticFriend3 getThis() {
        System.out.println(0) ;
        return this ; }
    StaticFriend3() { } //  Shouldn't be needed, but is currently.
}

public class Static3 {
    static StaticFriend3 p ;

    public static void main() {
        p = new StaticFriend3() ;
        p.f = p.f + 1 ;
        (p).f = (p).f + 1 ;
        p.getThis().f = (p.getThis()).f + 1 ;
        System.out.println( p.f ) ;
    }
}