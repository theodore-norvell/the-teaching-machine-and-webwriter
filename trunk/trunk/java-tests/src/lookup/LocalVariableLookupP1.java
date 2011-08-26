//! Compile. Expect no error.

package lookup;

public class LocalVariableLookupP1 {

    int i = 0 ;
    void m() {
        i = 4 ;
        {
            i = 1 ; // Should be the outer i
            int i = 2 ;
            i = 3 ; // Should be the inner i
            class Inner
            {
            	void f(){
	            	int i = 6;  // legitimate local variable
	            	i = 8;	// Should be f's i
            	}
            }
            i = 9;	// Should be the inner i
        }
        i = 10;	// Should be the outer i
    }
    
    public static void main() {
        LocalVariableLookupP1 p = new LocalVariableLookupP1() ;
        p.m() ;
    }
}