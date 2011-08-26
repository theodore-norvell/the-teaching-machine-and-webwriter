//! Compile. Expect no error
package functions;

public class Override00 {
    
    int func(int ch) {
        return 42 ;
    }
}

class Override00Child extends Override00 {

    int func(int ch) {
        return 28 ;
    }
}
