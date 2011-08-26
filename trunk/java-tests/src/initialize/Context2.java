//Test reporting of context errors.

//! Compile expect error ~
//! /.*line 10.*Can't refer to an instance variable in a static context.*/

package initialize;

public class Context2 {
    static int i ;
    static { i = j ; } // Error on this line
    int j ;
}