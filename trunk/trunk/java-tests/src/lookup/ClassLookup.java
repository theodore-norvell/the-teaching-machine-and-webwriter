//! Compile. Expect no error.

package lookup;

// Check the lookup of local classes
public class ClassLookup {
    
    static class A {
        int i ;
        public A() { }
     }
        
    void f() {
    
        A a0 = new A() ;
        a0.i = 0 ;
        
        class A {
            int j ; } 
        
        A a1 = new A() ;
        a1.j = 1 ;
        a0.i = 0 ; }
        
}