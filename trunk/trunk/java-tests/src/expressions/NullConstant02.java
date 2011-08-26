//! Compile. Run. Expect output = "42".
package expressions;

public class NullConstant02 {
  public static void main( String[] args ) {
      foo( null ) ;
  }
  
  private static void foo( NullConstant02 p ) {
      if( p == null ) System.out.println( "42" ) ;
  }
}