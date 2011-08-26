package statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class While3 {

    static public void main() {
        int i ;
        i = 0 ;
        alpha: while( i < 4 ) {
            int k ; k = 0 ;
            beta: while( k < 4 ) {
                if( i==0 && k==1 ) { k = 3 ; continue beta ; }
                if( i==0 && k==3 ) { i = 1 ; break beta ; }
                if( i==2 && k==0 ) { i = 3 ; continue alpha ; }
                if( i==3 && k==0 ) { break alpha ; }
                k++ ; }
            i++ ; }
        gamma : {
            i = 9 ;
            delta: if( i==9) break gamma ;
            i = -1 ;
        }
        i = 20 ;
    }
}