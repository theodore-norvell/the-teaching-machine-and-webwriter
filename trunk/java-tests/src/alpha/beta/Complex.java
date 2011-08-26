// A complex but self-contained compilation unit.
// Intended to exercise the first, second, and third
// passes.
// Intended to be compile-time error free.

package alpha.beta;

public 
final 
class Complex extends Ginger implements Henry, Ingrid {
    // Fields
    ;
    static final int TEN = 10 ;
    /* A trianglular matrix */
    static int tri[][] = new int[TEN][] ;
    static {
        for( int i=0 ; i<TEN ; ++i ) {
            tri[i] = new int[i] ;
            byte j ;
            for( j = 0 ; j < i ; j++ ) {
                tri[i][j] = i-j ; } }
    }
    
    Ginger field0 ;
    int i ;
    
    // Constructor
    Complex() {}
    
    // Constructor
    Complex(int i) { super(i) ;
        field0 = new Complex() ;
        this.i = 10 ;
        super.i = 11 ; 
    }
    
    // Nested class
    private class NestedClass {
        NestedStaticClass nsc = new NestedStaticClass() ;
        int i ;
        void nestedMethod() {
            field0.method0() ;
            i = 12 ;
            Complex.this.i = 13 ;
            Complex.super.i = 14 ;}
                
    }
    
    // Nested static class
    static protected class NestedStaticClass {}
    
    void method0() {
        Ginger h = new Ginger() {
            int i ;
            void method0() {} } ;
    }
    
    public int method1() {
        //
        final class LocalClass {
            int i ; }
        LocalClass lc = new LocalClass() ;
        lc.i = 20 ;
        return -99 ;
    }
    public char method2() {
        return 'a' ;
    }
}
    
    
    
abstract class Ginger {
    int i ;
    
    // Constructor
    Ginger( ) { this.i = 0 ; }
    // Constructor
    Ginger( int i ) { this.i = i ; }
    
    abstract void method0() ;
}

interface Henry extends Ingrid {
    int method1() ;
}

interface Ingrid {
    char method2() ;
} 
  