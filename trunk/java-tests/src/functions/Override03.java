//! Compile. Expect error matches /.*line 12.*TODO.*/
package functions;

public class Override03 {
    
    int func(int ch) {
        return 42 ;
    }
}

class Override03Child extends Override03 {

    private int func(int ch) {
        return 28 ;
    }
}
