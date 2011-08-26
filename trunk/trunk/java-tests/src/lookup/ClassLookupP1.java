//! Compile. Expect no error.

package lookup;

// Check the lookup of local classes
public class ClassLookupP1 {
    
    class A {
        int i ;
        public A() {}
     }
        
    void f() {
    
        A a0 = new A() ;
        a0.i = 0 ;
    }
        
}