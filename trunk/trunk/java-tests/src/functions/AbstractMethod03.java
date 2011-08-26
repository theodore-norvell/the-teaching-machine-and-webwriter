// Test that it is an error to have an abstract method in a non abstract class
//! Compile. Expect error matches /.*line 10.*TODO.*/
package functions;

 public interface AbstractMethod03 {
  
  abstract void foo( ) ;
}

class AbstractMethodChild03 implements AbstractMethod03 {
    void bar() { }
}