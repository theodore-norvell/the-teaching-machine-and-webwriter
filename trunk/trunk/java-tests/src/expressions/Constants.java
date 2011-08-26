//! Compile Run
package expressions;

public class Constants {
    public static void main() { // ADD argument later
        Constants p ;
        // null
        p = null ;

        // Decimal literals
        long i ;
        i = 0 ;
        i = 123 ;
        i = 123L ;
        i = 123l ; // That's an ell at the end!
        i = 2147483647 ;
        //X i =  - 2147483648 ; // Should be legal, but not yet supported.
        i = 9223372036854775807L ;
        //X i =  - 9223372036854775808L ; // Should be legal, but not yet supported.

        // Octal literals
        i = 0123 ;
        i = 00 ;
        i = 0432L ;
        i = 017777777777 ; // Most positive int
        i = 020000000000 ; // Most negative int
        i = 037777777777 ; // -1
        i = 0777777777777777777777L ; // Most positive long
        i = 01000000000000000000000L ; // Most negative long
        i = 01777777777777777777777L ; // -1L

        // Illegal Octal constants
        // i = 040000000000 ; // Too many bits
        // i = 02000000000000000000000L ; // Too many bits

        i = 0x0 ;
        i = 0xabcdef;
        i = 0xABCDEF;
        i = 0x40 ;
        i = 0xFFFFFFFF ;
        i = 0x7fffffff ; // Most positive int
        i = 0x80000000 ; // Most negative int
        i = 0xffffffff ; // -1
        i = 0x7fffffffffffffffL ; // Most positive long
        i = 0x8000000000000000L ; // Most negative long
        i = 0xffffffffffffffffL ; // -1L

        // Illegal Hex constants
        // 0x100000000 ; // Too many bits
        // 0x10000000000000000L ; // Too many bits

        // Float
        double x ;
        x = 3.40282347e+38f ; // Largest float
        x = 1.40239846e-45f ; // Epsilon
        x = .3f ;
        x = 2.f ;
        x = 1e1f ;

        // Double
        x = 1.79769313486231570e+308 ; // Largest double
        x = 4.94065645841246544e-324 ; // Double epsilon
        x = .3 ;
        x = 2. ;

        // Booleans
        boolean b ;
        b = true ;
        b = false ;

        // Characters.
        char c ;
        c = 'a' ;
        c = '\u0041' ;
        c = '\101' ;
        c = '\77' ;
        // \u0027\u002F\u0027\u0027 ; // Should be same as '\''
        c = '\b' ;
        c = '\t' ;
        c = '\n' ;
        c = '\f' ;
        c = '\r' ;
        c = '\"' ;
        c = '\'' ;
        c = '\\' ;
        c = '\101' ;
        c = '\60' ;
        c = '\060' ;
        c = '\7' ;


        // String
        String s ;
        s = "abcd\b\t\n\f\r\"\'\\\101\60\060\7" ;
        s = "" ;
    }
}