//Test the execution an explicit super call
//! run expect output = "6" endl "12" endl
package construction;

/**
* @author theo
*
*/

class Parent6 {
  protected int i = 6 ;
  
  Parent6() {
      System.out.println( i ) ;
  }
}

public class Constructor06 extends Parent6 {
    Constructor06 () {
        super() ;
        i = i * 2 ;
        System.out.println( i ) ;
    }
    
    public static void main(/*String[] args*/) {
        Constructor06 p = new Constructor06() ;
    }
}
