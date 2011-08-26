//! Compile. Expect no error.

package lookup;

interface I1 {
   class C{}
}

interface I2 {
    class C{} 
}
public class MulitpleInheritance implements I1, I2 {
    I1.C c1 ;
    I2.C c2 ;
    // C c3 ;  // reference to C is ambiguous
}