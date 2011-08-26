// Multiple inheritence. Methods with same name and parameters, but different throws clause.
// The difference between this one Multiple04 is that here when we come to the concrete
// implementation we do not respect both exception clauses. Thus an error.
//! Compile; expect error matches /.*line 29.*TODO*./

package functions;

class LeftException05 extends Exception {
    
}

class RightException05 extends Exception {
    
}

interface LeftInterface05 {
    public void foo() throws LeftException05; 
}


interface RightInterface05 {
    public void foo() throws RightException05 ;
}

public interface Multiple05 extends LeftInterface05, RightInterface05 {
}

class TryIt05 implements Multiple05 {
    public void foo() throws LeftException05 {}
}
