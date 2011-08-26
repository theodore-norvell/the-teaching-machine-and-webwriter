//Test reporting of context errors.

//! Compile expect no error

package initialize;

public class Context5 {
    static int i = 6 ;
    { j = i +6 ; }
    int j ;
}