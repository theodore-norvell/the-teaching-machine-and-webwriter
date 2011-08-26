package parsertest.statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Finally1 {
    public static void main() {
        int i ;
        i = 0 ;
        try {
            int j ;
            j = 1 ;
            if( i == j-1 ) return ; }
        finally {
            i = 1 ; }
        i = -1 ;
    }

}