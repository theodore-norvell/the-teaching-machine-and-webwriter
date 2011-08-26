package parsertest.statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Switch6 {
    public static void main() {
        int k ;
        for( char i=0 ; i < 3 ; ++i ) {
            switch( i ) {
            case 0 : {
                int j ;
                k = 0  ; }
            default: {
                int j ;
                k = 2 ; }
            case 1 : {
                int j ;
                k = 1 ; } }
            k = 3 ;
        }
     }

}