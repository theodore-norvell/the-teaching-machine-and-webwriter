// Test reporting of context errors.

//! Compile expect error ~
//! /.*line 9.*Can't refer to an instance variable in a static context.*/

package initialize;

public class Context1 {
    static int i = j; // Error on this line
    int j ;
}