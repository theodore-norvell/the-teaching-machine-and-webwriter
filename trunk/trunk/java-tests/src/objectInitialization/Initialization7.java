//! Run. Expect output "0" endl "3" endl "100" endl "100" endl "100" endl

//Test explicit this call.

package objectInitialization;

class Parent7 {
    { System.out.println(0) ; }
}

public class Initialization7 extends Parent7 {
  
  int j = 3 ;
  { System.out.println( j ) ; }
  
  Initialization7() {
      this('a') ;
      System.out.println(j) ;
  }
  
  Initialization7( char c) {
      this( (int) c ) ;
      System.out.println(j) ;
  }
  
  Initialization7( int i) {
      j = j+i ;
      System.out.println(j) ;
  }
  
  
  
  public static void main(/*String[] args*/) {
      Initialization7 p = new Initialization7() ;
  }
}