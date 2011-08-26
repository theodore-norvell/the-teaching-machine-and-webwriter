package parsertest.statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NestedFinallys {
    public static void main()  {
        boolean x = true ;
        boolean y = true ;
        int i = -1 ;
        try {
            i = 0 ;
            if( x ) return ; }
        finally { // Status := A
            try {
                i = 1 ; }
            finally { // Status := normal, return
                try {
                    inner : {
                        try {
                            i = 2 ;
                            if( y ) break inner ;
                            i = -1 ; }
                        finally { // Status := B, normal, A
                            i = 3 ;
                        } // Status := normal, A
                        i = -2 ; }
                    i = 4 ; }
                finally { // Status := normal, normal, A
                    i = 5 ;
                } // Status := normal, A
                i = 6 ;
            } // Status := A
            i = 7 ;
        } // Status := empty
        i = -3 ;
  }
}