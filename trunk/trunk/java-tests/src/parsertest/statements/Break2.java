package parsertest.statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Break2 {
    public static void main() {
        int i ; i = 0 ;
        while( true ) {
            alpha : {
                beta: if( i==0 ) break gamma; // Bad label
                i = 1 ; }
            i = 2 ; }
        i = 3 ;
    }
}