//Test the execution of a simple constructor with parameters
//! run expect output = "5" endl
package construction;

/**
* @author theo
*
*/
public class Constructor02 {
  Constructor02 (int i, int j) {
      int k = i+j ;
      System.out.println( k ) ;
  }

  public static void main( /*String[] args */ ) {
      Constructor02 p = new Constructor02(3, 2) ;
  }
}
