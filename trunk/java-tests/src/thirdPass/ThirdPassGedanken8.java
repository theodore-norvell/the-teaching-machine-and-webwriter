//! Compile. Expect error matches
//!        /.*Compilation error: scope of type thirdPass.A8/
//!        / cannot inherit from a Type that inherits from it.*/.
package thirdPass;
public class ThirdPassGedanken8 {
}

// Simple test of circular inheritence
class A8 extends B8 {
}

class B8 extends A8 {
}