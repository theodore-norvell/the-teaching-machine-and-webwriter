// Multiple inheritence. Methods with
// same name and parameters, but different throws clause.
// By my reading of the JLS 2.0, this is not allowed. Eclipse compiler is accepting it!
// Actually it makes sense that it is allowed.  Any implementation must respect both throws clauses.
// This is a very devilish test case. See also Multiple05.
//! Compile; expect no error.

package functions;

class LeftException04 extends Exception {
    
}

class RightException04 extends Exception {
    
}

interface LeftInterface04 {
    public void foo() throws LeftException04; 
}


interface RightInterface04 {
    public void foo() throws RightException04 ;
}

public interface Multiple04 extends LeftInterface04, RightInterface04 {
}

class TryIt04 implements Multiple04 {
    public void foo()  {}
}
