//Check code in a non static method
//! compile run expect output "17" endl
package staticContext;

public class StaticContext1p {
  int i ;
  
  StaticContext1p() { }
  
  void method() {
      int k ;
      i = 17 ; }  
  
  public static void main(/*String[] args*/) {
      StaticContext1p p = new StaticContext1p() ;
      p.method() ;
      System.out.println( p.i ) ;
  }
}