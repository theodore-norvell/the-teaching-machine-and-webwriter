package initialize;

//Test initialization of the main class.
//! Run expect output = "1" endl "2" endl

public class Initialize2 {
    static int k = 0 ;
    static {
        System.out.println( 1 ) ;
        k = 1 ;
        }
    static int x = k + 1 ;
    static {}
        
    
    public static void main() {
        System.out.println( 2 ) ;
        k = 3 ;
        return ;
    }
}