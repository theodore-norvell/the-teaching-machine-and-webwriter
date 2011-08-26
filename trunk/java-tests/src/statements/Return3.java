package statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Return3 {
    Return3() {}

    void test1() { return 1; } // X expression should not be there

    static public void main() {
        int i ;
        Return3 p ;
        p = new Return3() ;
        p.test1() ;
    }
}