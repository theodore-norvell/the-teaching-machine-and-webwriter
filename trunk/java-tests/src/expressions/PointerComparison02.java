//! Compile. Expect error matching /.*line 10.*Neither reference type can be converted to the other.*/
package expressions;

public class PointerComparison02 {
  public static void main( /* String[] args */ ) {
      PointerComparison02 p = null ;
      PointerComparison02LeftChild q = null ;
      PointerComparison02RightChild r = null ;
      
      if( q != r )  ;
  }
}

class PointerComparison02LeftChild extends PointerComparison02 {
    
}


class PointerComparison02RightChild extends PointerComparison02 {
    
}