// Test that it is an error to have an abstract method in a non abstract class
//! Compile. Expect error matches /.*line 7.*abstract not allowed.*/
package functions;

public class AbstractMethod01 {
  
  abstract void foo( ) ;
}