// Test the call of a function through the VFN. Implicet and explicet "this".
// This is the same as VirtualFunctions03, except that the object now
// belongs to the parent class alone.
//! Run. Expect output = "-1" endl
//!                         "1" endl
//!                            "-2" endl
//!                            "-2" endl
//!                         "1" endl
//!                            "-2" endl
//!                            "-2" endl
package functions;

public class VirtualFunctions04 {
  public static void main( /*String [] args*/ ) {
      VirtualFunctions04 p = new VirtualFunctions04() ;
      p.foo() ; // foo is defined in both. Parent's foo should be called.
  }
  
  void foo( ) {
      System.out.println( -1 ) ;
      bar() ; // Side call. Bar is defined only in the parent
      this.bar() ; // Side call. Bar is defined only in the parent
  }
  
  void bar() {
      System.out.println( 1 ) ;
      func() ; // Side call. func is defined in both.
      this.func() ;  // Side call. func is defined in both.
  }
  
  void func() {
      System.out.println( -2 ) ;
  }
}

class Child04 extends VirtualFunctions04{
    void func() {
        System.out.println( 2 ) ;
    }
    
    void foo() {
        System.out.println( 0 ) ;
        bar() ; // Up call. Bar is defined only in the parent
        this.bar() ; // Up call. Bar is defined only in the parent
    }
}
