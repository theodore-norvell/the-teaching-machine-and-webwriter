//! Compile. Expect error matches /.*line 13.*TODO.*/
package functions;

public class Override01 {
    
    int func(int ch) {
        return 42 ;
    }
}

class Override01Child extends Override01 {

    byte func(int ch) {
        return 28 ;
    }
}
