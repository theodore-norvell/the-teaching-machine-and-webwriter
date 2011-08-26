//Test the call of a function through the VFN
//! Run. Expect output = "84" endl ;
package functions;

public class VirtualFunctions02 {
  public static void main( /*String [] args*/ ) {
      VirtualFunctions02 p = new Child02() ;
      p.func() ;
  }
  
  void func() {
      System.out.println( 42 ) ;
  }
}

class Child02 extends VirtualFunctions02{
    void func() {
        System.out.println( 84 ) ;
    }
}
