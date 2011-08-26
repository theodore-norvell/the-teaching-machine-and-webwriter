//! Compile. Expect error matches /.*line 11/
//!   /.*No such constructor is accessable or applicable.*/

//Default initializer is missing.

package objectInitialization;


public class Initialization9a  {
  
  public Initialization9a() { this( 1.2 ) ; }
  public Initialization9a( int i ) { }
  public Initialization9a( char c ) { }
  
  public static void main(/*String[] args*/) {
      Initialization9a p = new Initialization9a() ;
  }
}
