//Check code in a "this" explicet constructor call.
//
//      According to 8.1.2 (JLS 2.0) the arguments of
//    a "this" explicit constructor call are in a
//    nonstatic context.
//      However 8.8.5.1 says that the argument of a
//    this explicit constructor can not contain
//    references to instance variables or nonstatic
//    methods.  It's not clear to me the difference!
//      So how can we test that that
//    the context is nonstatic?
//      This particular test should generate a compile
//    time error by 8.8.5.1, if not by 8.1.2.
//      I can't see anything wrong with just labelling
//    the "this" statement as a static context?
//      Importance of generating this error message: Very low.
//
//! compile expect error ~ /.*line 29.*TODO.*/
package staticContext;

public class StaticContext7 {
    int j =  17 ;
    int i  ;
    
    StaticContext7( int k ) {
        i = k+17 ; }
    
    StaticContext7() {
        this(method()) ; // Error on this line
    }
    
    int method() { return j ; }
    
    public static void main(/*String[] args*/) {
        StaticContext7 p = new StaticContext7() ;
    }
}