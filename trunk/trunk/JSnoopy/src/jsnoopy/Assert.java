package jsnoopy;

public class Assert
{
    public static void check( boolean prop ) {
        check( prop, "" ) ; }

    public static void check( boolean prop, String str ) {
        if( ! prop ) { throw new AssertException( str ) ; } }
}

class AssertException extends IllegalStateException {
    AssertException( String str ) {
        super( str ) ; }
}