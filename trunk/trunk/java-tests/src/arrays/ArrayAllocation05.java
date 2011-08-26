// Test use of computed size
//! run; expect output = "6" endl "7" endl "8" endl

package arrays;

public class ArrayAllocation05 {
  public static void main( String[] args ) {
      int size = 7;
      int [] A = new int[6];
      System.out.println( A.length ) ;
      int [] B = new int[size] ;
      System.out.println( B.length ) ;
      int [] C = new int[size*2 - 6] ;
      System.out.println( C.length ) ;
  }
}