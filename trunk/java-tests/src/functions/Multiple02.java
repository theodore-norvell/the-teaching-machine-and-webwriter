// Multiple inheritence. This time there is no root declaration.
// Well, I thought this was an error. But it seems not!
//! Compile; expect no error.

package functions;


interface LeftInterface02 {
    public void foo() ; 
}


interface RightInterface02 {
    public void foo() ;
}

public class Multiple02 implements LeftInterface02, RightInterface02 {
    public void foo() {}
}
