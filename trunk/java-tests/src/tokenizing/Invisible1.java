/*
 * Created on 30-May-08 by Theodore S. Norvell. 
 */

//! Compile. Run.
//! Expect output equals "0" endl "10" endl "20" endl "30" endl "40" endl.

package tokenizing;

public class Invisible1 {

    public static void main(String args ) {
        System.out.println(0) ;   /*#I System.out.println(10) ; System.out.println(20) ; System.out.println(30) ; */
        System.out.println(40) ;
    }
}
