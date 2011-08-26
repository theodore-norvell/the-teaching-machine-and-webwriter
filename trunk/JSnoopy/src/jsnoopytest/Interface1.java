package jsnoopytest;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

public interface Interface1 {
    void a( int i, String s ) ;
    void b( String nulll, boolean t, boolean f, byte byt,
            char c0, char c2) ;
    void c( double zero, double pos, double neg,
            double max, double min, double nan,
            double neginf, double posinf ) ;
    void d( float zero, float pos, float neg,
            float max, float min, float nan,
            float neginf, float posinf ) ;
    void e( int i0, int i1, int i2, int i3,
            long l0, long l1, long l2, long l3,
            short s0, short s1, short s2, short s3) ;
    void f( String str0, String str1, String str2 ) ;
    void g( int[] a0, int[] a1, int[] a2 ) ;
    void h( short[] a0, short[][] a1, String[] a2 ) ;
    void i( Integer i ) ;
    void z( TestMain bad ) ;
}