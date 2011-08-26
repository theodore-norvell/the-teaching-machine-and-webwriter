// Test that it is an error to instantiate an abstract class.
//! Compile. Expect error matches /.*line 10.*TODO.*/
package functions;

abstract public class AbstractMethod00 {
  
  abstract void foo( ) ;
  
  void bar() {
      AbstractMethod00 p = new AbstractMethod00() ;
  }
  
  abstract void func() ;
}