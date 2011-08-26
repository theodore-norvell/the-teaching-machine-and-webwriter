package parsertest.statements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Return2 {
    Return2() {}

    int test1() { return ; } // X Missing expression

    static public void main() {
        int i ;
        Return2 p ;
        p = new Return2() ;
        i = p.test1() ;
    }

}