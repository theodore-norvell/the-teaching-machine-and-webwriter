// Diamond inheritence. All declarations of foo refer to the same root declaration.
//! Compile; expect no error.

package functions;

interface BaseInterface01 {
    public void foo() ;
}

interface LeftChildInterface01 extends BaseInterface01 {
    public void foo() ;
}


interface RightChildInterface01 extends BaseInterface01 {
}

public class Multiple01 implements LeftChildInterface01, RightChildInterface01 {
    public void foo() {}
}
