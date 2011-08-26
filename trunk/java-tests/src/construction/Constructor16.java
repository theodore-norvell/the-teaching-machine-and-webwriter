//Test missing outer class
//! compile expect error ~ /.*TODO.*/
package construction;

/**
* @author theo
*
*/

class Constructor16 {
  int z ;
  Constructor16( int j ) { z = j ; }
  
  class Parent16 {
      int i ;
      Parent16(int m) {
          i = m+z ;
      }
  }
  
  public class Inner16 extends Constructor16.Parent16 {
      Inner16 () {
          super(4) ;
          // System.out.println( i ) ;
      }
  }
  
  public static void main(/*String[] args*/) {
      Constructor16 outer = new Constructor16(38) ;
      Inner16 p = new Inner16( ) ; // Error on this line
  }
}