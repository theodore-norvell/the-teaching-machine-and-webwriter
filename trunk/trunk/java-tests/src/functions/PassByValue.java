// Pass by value.
//! Compile. Run. Expect output = "997" endl "998" endl ;

package functions ;

public class PassByValue {
    public static void foo(int i, int j) {
        System.out.println(i) ; 
        System.out.println(j) ; 
    }
    
    public static void main( String [] args ) {
        int i = 997 ;
        foo( i, ++i ) ;
    }
}
