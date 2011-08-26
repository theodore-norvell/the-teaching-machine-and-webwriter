//Check code in a static initialization block.
//  Violates 6.5.6 (JLS 2.0)
//! compile expect error ~ /.*line 9.*Can't refer to an instance variable in a static context.*/
package staticContext;

public class StaticContext4e0 {
  int i ;
  static { 
      {i = 17 ;} // Error on this line
  }
  
  StaticContext4e0() {
      int k ; }
  
  public static void main(/*String[] args*/) {
      StaticContext4e0 p = new StaticContext4e0() ;
  }
}