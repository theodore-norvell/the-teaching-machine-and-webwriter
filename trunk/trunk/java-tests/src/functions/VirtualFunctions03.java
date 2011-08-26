// Test the call of a function through the VFN. Implicet and explicet "this"
//! Run. Expect output = "0" endl
//!                         "1" endl
//!                            "2" endl
//!                            "2" endl
//!                         "1" endl
//!                            "2" endl
//!                            "2" endl
package functions;

public class VirtualFunctions03 {
  public static void main( /*String [] args*/ ) {
      VirtualFunctions03 p = new Child03() ;
      p.foo() ; // foo is defined in both. Child's foo should be called.
  }
  
  void foo( ) {
      System.out.println( -1 ) ;
  }
  
  void bar() {
      System.out.println( 1 ) ;
      func() ; // Down call. func is defined in both.
      this.func() ;  // Down call. func is defined in both.
  }
  
  void func() {
      System.out.println( -2 ) ;
  }
}

class Child03 extends VirtualFunctions03{
    void func() {
        System.out.println( 2 ) ;
    }
    
    void foo() {
        System.out.println( 0 ) ;
        bar() ; // Up call. Bar is defined only in the parent
        this.bar() ; // Up call. Bar is defined only in the parent
    }
}
