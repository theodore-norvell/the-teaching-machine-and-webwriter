//! Compile. Expect no error.

package lookup;

public class FieldLookup {

    int y ;
    
    class Inner {
        int x ;
        void func() {
            x = 0 ;
            this.x = 1 ;
            y = 2;
            FieldLookup.this.y = 3 ;
            // this.y = 4 ;
        }
    }   
}



class Parent1 {
    int z ;
}

class Outer1 extends Parent1 {
    class Inner1 extends Parent1 {
        void func () {
            z = 0 ;
            this.z = 1 ;
            Outer1.this.z = 2 ;
            super.z = 3 ;
            Outer1.super.z = 4 ;
        }
    }
}