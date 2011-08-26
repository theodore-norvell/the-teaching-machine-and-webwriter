package jsnoopytest;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

public class Class1 implements Interface1 {
    private Interface2 ob2 ;

    Class1( Interface2 ob2 ) {
        this.ob2 = ob2 ; }

    public void a( int i, String s ) {
        jsnoopy.JSnoopy.appendCommentLn("First comment String");
        String[] sa = new String[i] ;
        boolean p = true ;
        for( int j=0 ; j<i ; ++j ) {
            ob2.c( p, new Boolean( ! p ) ) ;
            sa[j] = s ; }
        ob2.b( sa ) ;
    }

    public void b( String nulll, boolean t, boolean f, byte byt,
            char c0, char c2) {}
    public void c( double zero, double pos, double neg,
            double max, double min, double nan,
            double neginf, double posinf ) {}
    public void d( float zero, float pos, float neg,
            float max, float min, float nan,
            float neginf, float posinf ) {}
    public void e( int i0, int i1, int i2, int i3,
            long l0, long l1, long l2, long l3,
            short s0, short s1, short s2, short s3) {}
    public void f( String str0, String str1, String str2 ) {}
    public void g( int[] a0, int[] a1, int[] a2 ) {}
    public void h( short[] a0, short[][] a1, String[] a2 ) {}

    public void i( Integer i ) {
        if( TestMain.mode == TestMain.SHORT ) {
            jsnoopy.JSnoopy.appendCommentLn("Short mode comment line");
            ob2.d("a") ; }
        else if( TestMain.mode == TestMain.LONG ) {
            jsnoopy.JSnoopy.appendCommentLn("Long mode comment line");
            ob2.d("a") ;
            ob2.d("b") ;
            ob2.d("c") ; }
        else if( TestMain.mode == TestMain.VARIANT ) {
            jsnoopy.JSnoopy.appendCommentLn("Variant mode comment line");
            ob2.d("b") ;
            ob2.d("a") ; }
        else {
            jsnoopy.JSnoopy.appendCommentLn("Standard mode comment line");
            ob2.d("a") ;
            ob2.d("b") ; }
        jsnoopy.JSnoopy.appendComment("Last comment String");
    }

    public void z( TestMain bad ) {}
}