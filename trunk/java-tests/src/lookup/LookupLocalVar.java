//! Compile; expect no error.

package lookup;

public class LookupLocalVar {
  public LookupLocalVar() {
    boolean fred ;
    {
      fred = true ;
    }
  }
}