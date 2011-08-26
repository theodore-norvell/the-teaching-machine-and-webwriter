package statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Do1 {
    static public void main() {
        int i, j ;
        i = 0 ;
        j = 0 ;
        do {
            int k ;
            j += i ;
            if( i==5 ) { i = 8 ; continue ; }
            i ++ ;
        } while( i < 10 ) ;
        i = 20 ;
    }
}