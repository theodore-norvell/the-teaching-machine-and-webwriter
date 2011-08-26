//Test reporting of context errors.

//! Compile expect error ~
//! /.*line 11.*Can't refer to an instance variable in a static context.*/

package initialize;

public class Context3 {
    static int i ;
    static { 
        { i = j ; } // Error on this line
    } 
    int j ;
}