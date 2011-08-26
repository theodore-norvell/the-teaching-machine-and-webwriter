/*
 * Created on 30-May-08 by Theodore S. Norvell. 
 */

//! Compile. Run.
//! Expect output equals "0" endl "10" endl "20" endl "30" endl "40" endl.

package tokenizing;

public class Invisible {

    public static void main(String args ) {
        System.out.println(0) ;
/*#I        System.out.println(1) ;
*/        System.out.println(2) ;
/*#I        System.out.println(3) ; */
        System.out.println(4) ; /*#I
        System.out.println(5) ; */
        System.out.println(6) ;
        System.out.println(7) ; /*#I
        System.out.println(8) ;
*/    }
}
