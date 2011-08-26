// Test the call of a function through the VFN. Implicet and explicet "this"
// This time we use an abstract class.
//! Run. Expect output = "0" endl
//!                         "1" endl
//!                            "2" endl
//!                            "2" endl
//!                         "1" endl
//!                            "2" endl
//!                            "2" endl
package functions;

abstract public class VirtualFunctions05 {
  public static void main( /*String [] args*/ ) {
      VirtualFunctions05 p = new Child05() ;
      p.foo() ; // foo is abstract Child's foo should be called.
  }
  
  abstract void foo( ) ;
  
  void bar() {
      System.out.println( 1 ) ;
      func() ; // Down call. func is defined in both.
      this.func() ;  // Down call. func is defined in both.
  }
  
  abstract void func() ;
}

class Child05 extends VirtualFunctions05{
    void func() {
        System.out.println( 2 ) ;
    }
    
    void foo() {
        System.out.println( 0 ) ;
        bar() ; // Up call. Bar is defined only in the parent
        this.bar() ; // Up call. Bar is defined only in the parent
    }
}
