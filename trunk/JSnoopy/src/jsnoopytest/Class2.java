package jsnoopytest;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

public class Class2 implements Interface2 {

    private Interface3 ob3 ;

    public Class2( Interface3 ob3 ) {
        this.ob3 = ob3 ;
    }

    public int b(String[] sa) {
        ob3.d( sa ) ;
        return sa.length ; }

    public char c( boolean b1, Boolean b2) {
        try {
            if( b1 ) ob3.e() ;
            return 'a' ; }
        catch( Exception e ) {
            return 'b' ; } }

    public void d( String str ) {
    }
}