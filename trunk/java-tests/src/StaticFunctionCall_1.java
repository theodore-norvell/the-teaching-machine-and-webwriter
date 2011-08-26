//! compile run

import anotherPackage.ClassInAnotherPackage;

/** Test calls of static functions.
 * @author theo
 *
 */
public class StaticFunctionCall_1 {

    public static void main() {
        localFunction() ;
        //ClassInSamePackage.staticFunction() ;
        ClassInAnotherPackage.anotherStaticFunction() ;
    }
    
    static void localFunction() {}
}
