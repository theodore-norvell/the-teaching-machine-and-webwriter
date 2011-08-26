//! Compile.

package thirdPass;
class ThirdPassGedanken extends A {
    static class B extends C {
        D d ;
    }
}

class A {
    static class C {
        static class D {
           ThirdPassGedanken.B b ;
        }
    }
}