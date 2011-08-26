//! run. expect output = "3" endl "1" endl

package lookup;

public class LocalVariableLookupP0 {

    int i = 0 ;
    void m() {
        i = 4 ;
        {
            i = 1 ; // Should be the outer i
            int i = 2 ;
            i = 3 ; // Should be the inner i
            System.out.println( i ) ;
        }
        System.out.println( i ) ;
        return ;
    }
    
    /**
	 * 
	 */
	public LocalVariableLookupP0() {
		// TODO Auto-generated constructor stub
	}
    
    public static void main() {
        LocalVariableLookupP0 p = new LocalVariableLookupP0() ;
        p.m() ;
    }
}