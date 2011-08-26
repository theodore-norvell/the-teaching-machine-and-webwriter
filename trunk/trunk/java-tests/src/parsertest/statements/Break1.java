package parsertest.statements ;

public class Break1 {
    public static void main() {
        int i ; i = 0 ;
        while( true ) {
            alpha : {
                beta: if( i==0 ) break ;
                i = 1 ; }
            i = 2 ; }
        i = 3 ;
    }
}