//! Compile.

// I'm not so sure about this one.
// At some point I thought it should not compile.
// However, both the JDK compiler 1.4 and the eclipse 3.0 compiler
// have no problem compiling it.

package thirdPass;
public class ThirdPassGedanken6 {
}

class A6 extends B6.Nested {
}

class B6 extends C6 {
}

class C6 {
    static class Nested {
    }
}