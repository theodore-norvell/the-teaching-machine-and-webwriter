//Test the sequence that initializations happen in.

//! Run expect output = "0123456" endl

package initialize;

class K {
    static int k = 0 ;
    static { System.out.print( 0 ); }
}
public class Initialize1 extends Super1 implements SomeInterface1 {
    static {
        K.k = 2 ;
        System.out.print(2);
        }
    
    public static void main(/*String[] args*/) {
        //   Since SomeInterface1.one is a compile time constant
        // the next line should not trigger static initialization of
        // SomeInterface.
        int one = SomeInterface1.one ; 
        K.k = 3; 
        System.out.print(3);
        SomeClass1.x = SomeInterface1.a ;
        K.k = 5; 
        System.out.println(6);
    }
    static int init() {
        K.k = 3 ;
        System.out.print(4) ;
        return 0 ; }
}

class Super1 {
    static {
        K.k = 1 ;
        System.out.print( 1 );
    }
}
    

class SomeClass1 {
    static int x ;
    static {
        K.k = 4 ;
        System.out.print(5);
    }
}

interface SomeInterface1 {
    static final int one = 1 ;
    static final int a = Initialize1.init() ;
}