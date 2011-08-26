//! run. expect output = ""

package lookup;

public class LocalVariableLookupB {

    int i = 0 ;
    void m() {
        i = 4 ;
        {
            i = 1 ; // Should be the outer i
            int j = 2 ;
            j = 3 ; // Should be the inner i
        }
    }
    
    public static void main() {
        LocalVariableLookupB p = new LocalVariableLookupB() ;
        p.m() ;
    }
}