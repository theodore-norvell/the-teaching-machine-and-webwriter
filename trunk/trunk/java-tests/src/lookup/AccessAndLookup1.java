//! Compile. Expect error matches
//!   /.*line 7.*lookup\.Parent1\.x is inacessible.*/

package lookup;
public class AccessAndLookup1 extends Parent1 {
  void func0() {
    p.x = 2 ; // Error.  Attempt to access private field
  }
  AccessAndLookup1 p ;
}


class Parent1 extends GrandParent1 {
  private boolean x ;
}

class GrandParent1 {
  protected class ANestedClass {}
  protected int x ;
}