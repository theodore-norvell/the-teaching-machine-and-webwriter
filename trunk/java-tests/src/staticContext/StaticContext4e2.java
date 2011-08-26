//Check code in a static initialization block
//Violates 15.8.3 (JLS 2.0)
//! compile expect error ~ /.*line 9.*Can't use "this" in a static context.*/
package staticContext;

public class StaticContext4e2 {
  int i ;
  static { 
      this.i = 17; // Error on this line
  }
  void method() { }
  
  StaticContext4e2() {
      int k ; }
  
  public static void main(/*String[] args*/) {
      StaticContext4e2 p = new StaticContext4e2() ;
  }
}