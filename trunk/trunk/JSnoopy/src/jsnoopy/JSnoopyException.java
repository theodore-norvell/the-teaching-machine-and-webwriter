package jsnoopy;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

public class JSnoopyException extends Exception {

    private Throwable throwable ;

    public JSnoopyException() {
        super() ;
    }
    public JSnoopyException( String message ) {
        super( message ) ;
    }
    public JSnoopyException( Throwable t ) {
        super( t.getMessage() ) ;
        this.throwable = t ; }

    public Throwable getWrappedThrowable() {
        return throwable ; }

    public static JSnoopyException wrap( Throwable t ) {
        if( t instanceof JSnoopyException ) return (JSnoopyException) t ;
        else return new JSnoopyException( t ) ; }
}