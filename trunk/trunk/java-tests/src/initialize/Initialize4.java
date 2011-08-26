package initialize;

//Test initialization of a class prior to a variable reference
//! Run expect output = "2" endl
class Friend4 {
    static int k = 1;
    static {
        int j = 2 ;
        k = j ;
        System.out.println( k ) ;
    }
}

public class Initialize4 {
    
    public static void main() {
        Friend4.k = 2 ;
        Friend4.k = 2 ;
        return ;
    }
}