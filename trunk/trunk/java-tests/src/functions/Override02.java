//! Compile. Expect no error.
package functions;

public class Override02 {
    
    int func(int ch) {
        return 42 ;
    }
}

class Override02Child extends Override02 {

    public int func(int ch) {
        return 28 ;
    }
}
