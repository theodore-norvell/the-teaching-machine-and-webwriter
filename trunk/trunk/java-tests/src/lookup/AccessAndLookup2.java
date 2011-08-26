//! Compile expect no error.

package lookup;

public class AccessAndLookup2 extends Parent2 implements SomeInterface2 {
  void func0() {
    boolean y ;
    y = x ;// Legal. But if the private x is made public or protected, then
            // this is ambiguous.  See JLS 6.5.6.1.
    y = p.x ;// Legal. But if the private x is made public or protected, then
            // this is ambiguous.  See JLS 6.5.6.2
  }
  AccessAndLookup2 p ;
}

class Parent2 {
  private static final int x = 1 ;
}

interface SomeInterface2 {
  public static final boolean x = true ;
}