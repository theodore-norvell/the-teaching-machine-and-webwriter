//! run. expect output = "46" endl.

// Test lookup of inherited static fields.
package staticVariables;

/**
 * @author theo
 *
 */
class Parent4 {
    static int f = 40 ;
    Parent4() {f++; }
}

class Child4 extends Parent4 {
    Child4() {int x = 0; } 
    Child4 getThis() { return this ; }
}

public class Static4 extends Parent4 {

    public static void main(/*String [] args*/) {
        f = f + 1 ;
        Parent4.f = Parent4.f + 1 ;
        Child4.f = Child4.f + 1 ;
        
        Child4 child = new Child4() ;
        child.f = child.f + 1 ;
        child.getThis().f = child.getThis().f + 1 ;
        System.out.println( f ) ;
    }
}