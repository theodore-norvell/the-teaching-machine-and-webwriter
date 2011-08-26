//Check code in a static field initializer
//Violates 15.8.3 (JLS 2.0)
//! compile expect error ~
//! /.*line 9.*Can't use "this" in a static context.*/
package staticContext;

public class StaticContext5e2 {
  int j =  17 ;
  static int i = this.j ; // Error on this line
  
  StaticContext5e2() {
      int k ; }
  
  public static void main(/*String[] args*/) {
      StaticContext5e2 p = new StaticContext5e2() ;
  }
}