//Check code in an instance initialization block
//! compile run expect output "17" endl
package staticContext;

public class StaticContext3p {
  int i ;
  { 
      i = 17 ;
  }
  
  StaticContext3p() {
      int k ; }
  
  public static void main(/*String[] args*/) {
      StaticContext3p p = new StaticContext3p() ;
      System.out.println( p.i ) ;
  }
}