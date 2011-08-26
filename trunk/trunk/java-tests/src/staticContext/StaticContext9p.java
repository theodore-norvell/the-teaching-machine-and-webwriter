//Check code in a static method
//! compile run expect output "17" endl
package staticContext;

/**
* @author theo
*
*/
public class StaticContext9p {
  static StaticContext9p p = new StaticContext9p() ;
  
  StaticContext9p() { }
  
  void method() { System.out.println( 17 ) ; } 
  
  public static void main(/*String[] args*/) {
      p.method() ;
  }
}
