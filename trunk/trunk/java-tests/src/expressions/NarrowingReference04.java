//! Compile; expect error matching /.*line 10.*Cast can never succeed.*/
package expressions;

public class NarrowingReference04 {

    static public void main(){

        NarrowingReferenceLeftChild04 p = new NarrowingReferenceLeftChild04() ;
        NarrowingReferenceRightChild04 q ;
        q = (NarrowingReferenceRightChild04)  p ;
    }
}

class NarrowingReferenceLeftChild04 extends NarrowingReference04 {
}

class NarrowingReferenceRightChild04 extends NarrowingReference04 {
}