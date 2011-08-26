//Test the execution an implicit super call with an inner superclass
//! run expect output = "10" endl
package construction;

/**
* @author theo
*
*/

class Constructor10 {
  int z ;
  Constructor10( ) { z = 8 ; }
  
  class Parent10 {
      int i ;
      Parent10() {
          i = 2+z ;
      }
  }
  
  public class Inner10 extends Constructor10.Parent10 {
      Inner10 () {
          System.out.println( i ) ;
      }
  }
  
  public static void main(/*String[] args*/) {
      Constructor10 outer = new Constructor10() ;
      Inner10 p = outer.new Inner10( ) ;
  }
}