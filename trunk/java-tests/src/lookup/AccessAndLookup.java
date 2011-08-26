//! Compile. Expect error matches
//!   /.*line 7.*lookup\.Parent\.x is inacessible.*/

package lookup;
public class AccessAndLookup extends Parent {
  void func0() {
    x = 1 ; // Error.  Attempt to access private field
  }
  AccessAndLookup p ;
}


class Parent extends GrandParent {
  private boolean x ;
}

class GrandParent {
  protected class ANestedClass {}
  protected int x ;
}