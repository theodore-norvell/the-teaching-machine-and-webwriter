// Test calls via an interface
//! Run. Expect output = "0" endl "1" endl "2" endl "3" endl
//!                      "0" endl "1" endl "2" endl "3" endl.
package functions;

abstract public class VirtualFunctions06 {
  public static void main( /*String [] args*/ ) {
      VirtualFunctions06LeftChild left = new VirtualFunctions06LeftChild() ;
      go( left, left) ;
      VirtualFunctions06RightChild right = new VirtualFunctions06RightChild() ;
      go( right, right ) ;
      left.foo() ;
      left.bar() ;
      right.foo() ;
      right.bar() ;
      
  }
  
  public abstract void foo() ;
  
  static void go( VirtualFunctions06 p, VirtualFunctions06Interface q ) {
      p.foo() ;
      q.bar() ;
  }
}

interface VirtualFunctions06Interface {
    void bar() ;
}

class VirtualFunctions06LeftChild
        extends VirtualFunctions06
        implements VirtualFunctions06Interface
{
    public void foo() {
      System.out.println( 0 ) ;  
    }
    
    public void bar() {
        System.out.println( 1 ) ;  
      }
}

class VirtualFunctions06RightChild 
    extends VirtualFunctions06
    implements VirtualFunctions06Interface
{
    public void foo() {
      System.out.println( 2 ) ;  
    }
    
    public void bar() {
        System.out.println( 3 ) ;  
      }
}