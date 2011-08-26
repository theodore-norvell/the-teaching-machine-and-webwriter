//Test the execution of a simple constructor with a call to "this" constructor
//! run expect output = "5" endl "7" endl
package construction;

/**
* @author theo
*
*/
public class Constructor04 {
    int i ;
    int j ;
    int k = 5;
    
    Constructor04 () {
        i = 4 ;
        System.out.println( k ) ;
    }
    
    Constructor04 (int m) {
        this() ;
        j = m ;
        k = i + j ;
        System.out.println( k ) ;
    }

  public static void main(/*String[] args*/) {
      Constructor04 p = new Constructor04( 3 ) ;
  }
}
