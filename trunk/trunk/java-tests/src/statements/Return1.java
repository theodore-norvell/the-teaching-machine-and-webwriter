package statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Return1 {
    Return1() {}

    int test1() { return 1 ; }

    static public void main() {
        int i ;
        Return1 p ;
        p = new Return1() ;
        i = p.test1() ;
    }

}