package parsertest.statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Switch {
    public static void main() {
        int k ;
        for( int i=0 ; i < 3 ; ++i ) {
            switch( i ) {
            case 0 :
                int j = 0 ;
                k = 0  ;
            case 1 :
                // Jump past the decl of j.
                // Java seems to allow this.
                j = 4 ;
                k = 1 ; }
            k = 2 ;
        }
     }
}