//Check code in a nonstatic field initializer
//! run expect output "17" endl
package staticContext;

public class StaticContext6p {
  int j =  17 ;
  int i = valOfj() ;
  
  int valOfj() { return j ; }
  
  StaticContext6p() {
      int k ; }
  
  public static void main(/*String[] args*/) {
      StaticContext6p p = new StaticContext6p() ;
      System.out.println( p.i ) ;
  }
}