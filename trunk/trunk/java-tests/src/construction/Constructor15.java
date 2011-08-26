//Test report of missing super class
//! compile expect error ~ /.*TODO.*/
package construction;

/**
* @author theo
*
*/

class Outer15 {
  int z ;
  Outer15( int j ) { z = j ; }
  
  class Parent15 {
      int i ;
      Parent15(int m) {
          i = m+z ;
      }
  }
}

public class Constructor15 extends Outer15.Parent15 {
  Constructor15 (Outer15 outer) {
      super(3) ; // Error on this line
      // System.out.println( i ) ;
  }
  
  public static void main(/*String[] args*/) {
      Outer15 outer = new Outer15(42) ;
      Constructor15 p = new Constructor15( outer ) ;
  }
}