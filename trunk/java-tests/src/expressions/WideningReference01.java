//! Compile. Run. Expect output = "0" endl "1" endl "2" endl "3" endl "4" endl "4" endl
package expressions;

public class WideningReference01 {

    static public void main(){

        WideningReference01 p = new WideningReferenceChild01() ;
        WideningReferenceChild01 q = new WideningReferenceChild01() ;
        p = q  ;
        p = (WideningReference01) q ;
        p = (WideningReferenceChild01) q ;
        p = (expressions.WideningReferenceChild01) q ;
        if( p == q ) { System.out.println(0 ) ; }
        if( q == p ) { System.out.println( 1 ) ; }
        foo( p ) ;
        foo( q ) ;
        bar( p ) ;
        bar( q ) ;
    }
    
    static void foo( WideningReference01 x ) {
        System.out.println( 2 ) ;
    }
    
    static void foo( WideningReferenceChild01 x ) {
        System.out.println( 3 ) ;
    }
    
    static void bar( WideningReference01 x ) {
        System.out.println( 4 ) ;
    }
}

class WideningReferenceChild01 extends WideningReference01 {
    
}