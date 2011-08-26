// Test primitive type conversion in array initialization.
//! run; expect output = "3.0" endl
//!                      "-1.0" endl
//!                      "3.0" endl
//!                      "-1.0" endl
//!                      "3.0" endl
//!                      "-1.0" endl
//!                      "3.0" endl
//!                      "-1.0" endl
package arrays;

public class ArrayAllocation04 {
  public static void main( /*String[] args*/ ) {
      float a1[] = {3, -1 } ;
      double b1[] = {a1[0], a1[1] } ;
      
      float a2[] = new float[]{3, -1 } ;
      double b2[] = {a2[0], a2[1] } ;
      
      System.out.println( a1[0] ) ;
      System.out.println( a1[1] ) ;
      System.out.println( b1[0] ) ;
      System.out.println( b1[1] ) ;
      
      System.out.println( a2[0] ) ;
      System.out.println( a2[1] ) ;
      System.out.println( b2[0] ) ;
      System.out.println( b2[1] ) ;
  }
}