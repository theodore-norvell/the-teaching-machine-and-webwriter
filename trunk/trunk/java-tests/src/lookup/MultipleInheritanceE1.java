//! Compile. Expect error matches
//!    /.*line 16.*Ambiguous reference for C.*/

package lookup;

interface I11 {
   class C{}
}

interface I21 {
    class C{} 
}
public class MultipleInheritanceE1 implements I11, I21 {
    I11.C c1 ;
    I21.C c2 ;
    C c3 ;  // reference to C is ambiguous
}