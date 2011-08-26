//! Compile. Expect error matching /.*line 10.*conversion must be widening.*/
package expressions;

public class NarrowingReference01 {

    static public void main(){

        NarrowingReference01 p = new NarrowingReferenceChild01() ;
        NarrowingReferenceChild01 q  ;
        q = p ;
        
    }
}

class NarrowingReferenceChild01 extends NarrowingReference01 {
    
}