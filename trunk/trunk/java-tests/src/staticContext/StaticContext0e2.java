//Check code in a static method.
// Violates 15.8.3 (JLS 2.0)
//! compile expect error ~ 
//! /.*line 15.*Compilation error: Can't use "this" in a static context.*/

package staticContext;


public class StaticContext0e2 {
  int i ;
   
  void method() { }
  
  public static void main(/*String[] args*/) {
      {  this.i = 0 ; } // Error on this line
  }
}