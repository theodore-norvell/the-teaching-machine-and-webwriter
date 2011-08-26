// Test that it is an error to have an abstract method in a non abstract class
//! Compile. Expect error matches /.*line 10.*TODO.*/
package functions;

abstract public class AbstractMethod02 {
  
  abstract void foo( ) ;
}

class AbstractMethodChild02 extends AbstractMethod02 {
    void bar() { }
}