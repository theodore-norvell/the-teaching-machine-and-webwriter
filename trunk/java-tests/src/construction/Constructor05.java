//Test the execution an implicit super call
//! run expect output = "5" endl "10" endl
package construction;

/**
* @author theo
*
*/

class Parent5 {
    protected int i = 5 ;
    
    Parent5() {
        System.out.println( i ) ;
    }
}

public class Constructor05 extends Parent5 {
    Constructor05 () {
      i = i * 2 ;
      System.out.println( i ) ;
  }

  public static void main(/*String[] args*/) {
      Constructor05 p = new Constructor05() ;
  }
}
