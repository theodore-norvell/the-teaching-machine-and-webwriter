//! Compile. Expect error matches
//!          /.*Compilation error: Type thirdPass.A9 can't/
//!          / inherit from itself.*/.

package thirdPass;
public class ThirdPassGedanken9 {
}

// Simple test of circular inheritence
class A9 extends A9 {
}