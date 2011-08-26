// Multiple inheritence. Methods with
// same name and parameters, but different return types.
//! Compile; expect error mathes /.*line 18.*TODO*/

package functions;


interface LeftInterface03 {
    public void foo() ; 
}


interface RightInterface03 {
    public int foo() ;
}

public interface Multiple03 extends LeftInterface03, RightInterface03 {
}
