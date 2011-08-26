//! Compile. Expect error matches
//!   /.*line 13.*Unable to find superType I11*/

package lookup;

interface I1 {
   class C{}
}

interface I2 {
    class C{} 
}
public class MultipleInheritanceE0 implements I11, I21 {
    I11.C c1 ;
    I21.C c2 ;
    C c3 ;  // reference to C is ambiguous
}