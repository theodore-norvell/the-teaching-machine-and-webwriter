package statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Switch3 {
    public static void main() {
        int k ;
        for( int i=0 ; i < 3 ; ++i ) {
            s: switch( i ) {
            case 0 : {
                int j ;
                k = 0  ;
                break s ; }
            default: {
                int j ;
                k = 2 ;
                break s ; }
            case 1 : {
                int j ;
                k = 1 ; } }
            k = 3 ;
        }
     }
}