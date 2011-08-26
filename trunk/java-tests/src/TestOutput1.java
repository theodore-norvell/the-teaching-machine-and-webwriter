//! run expect output = "42" endl
//!                     "1000000" endl
//!                     "-1000000" endl
//!                     "1000" endl
//!                     "-1000" endl
//!                     "100" endl
//!                     "-100" endl
//!                     "1000000000000" endl
//!                     "-1000000000000" endl
//!                     "a" endl
//!                     "true" endl
//!                     "false" endl
//!                     "99.99" endl
//!                     "-99.99" endl
//!                     "9.9E201" endl
//!                     "-9.9E201" endl

import java.io.PrintStream ;

public class TestOutput1 {

    public static void main(/*String[] args*/) {
        PrintStream out = System.out ;
        out.print(42) ; out.println() ;
        
        int i = 1000000 ;
        out.println( i ) ;
        i = -1000000 ;
        out.println( i ) ;
        
        short s = 1000 ;
        out.println( s ) ;
        s = - 1000 ;
        out.println( s ) ;
        
        byte b = 100 ;
        out.println( b ) ;
        b = -100 ;
        out.println( b ) ;
        
        long l = 1000000000000L ;
        out.println( l ) ;
        l = -1000000000000L ;
        out.println( l ) ;
        
        char ch = 'a' ;
        out.println( ch ) ;
        
        boolean q = true ;
        out.println( q ) ;
        q = false ;
        out.println( q ) ;
        
        float f = 99.99F ;
        out.println( f ) ;
        f = - 99.99F ;
        out.println( f ) ;
        
        double d = 99E200 ;
        out.println( d ) ;
        d = - 99E200 ;
        out.println( d ) ;
        
    }
}