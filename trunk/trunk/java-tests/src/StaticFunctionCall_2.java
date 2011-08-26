//! compile. run.
/**
 * @author theo
 *
 */
public class StaticFunctionCall_2 {

    public static void main() {
        localFunction() ;
        ClassInSamePackage.staticFunction() ;
    }
    
    static void localFunction() {}

}
