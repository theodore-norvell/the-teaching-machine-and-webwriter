//! Compile. Expect error matches
//!   /.*line 17.*Duplicate local variable i*/

package lookup;

public class LocalVariableLookupE0 {

    int i = 0 ;
    void m() {
        i = 4 ;
        {
            i = 1 ; // Should be the outer i
            int i = 2 ;
            i = 3 ; // Should be the inner i
            {
            	i = 5; // Should be inner i
            	int i = 6;  // Duplicate local variable
            	i = 8;	// Should be inner-inner i
            }
            i = 9;	// Should be the inner i
        }
        i = 10;	// Should be the outer i
    }
    
    public static void main() {
        LocalVariableLookupE0 p = new LocalVariableLookupE0() ;
        p.m() ;
    }
}