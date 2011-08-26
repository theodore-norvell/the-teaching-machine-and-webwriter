// Test allocation and access to a single dimensional array
//! run expect output = "5" endl "5" endl "3" endl "3" endl "3" endl "3" endl "3" endl "3" endl
package arrays;

public class ArrayLength {
    static private ArrayLength[] B = new ArrayLength[3] ;
    
    public static void main( /*String[] args*/ ) {
        int [] A = new int[5] ;
        System.out.println( A.length ) ;
        System.out.println( (A).length ) ;
       
        System.out.println( B.length ) ;
        System.out.println( (B).length ) ;
        
        System.out.println( ArrayLength.B.length ) ;
        System.out.println( (ArrayLength.B).length ) ;
        
        System.out.println( arrays.ArrayLength.B.length ) ;
        System.out.println( (arrays.ArrayLength.B).length ) ;
     }
}