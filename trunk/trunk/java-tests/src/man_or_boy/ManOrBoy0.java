//! Run. Expect output is "-67.0" endl.
/*
 * Created on 19-Sep-07 by Theodore S. Norvell. 
 * See Donald Knuth (Jul 1964). Man or boy?, Algol Bullitin, 17.2.4, 1964.
 * http://archive.computerhistory.org/resources/text/algol/algol_bulletin/A17/P24.HTM
 */
package man_or_boy;

interface Thunk0 {
    float value() ;
}

class MutInt0 {
    public int i ;
    
    public MutInt0(int i0) {
        i = i0 ;
    }
    
}

public class ManOrBoy0 {
    static float a( final MutInt0 k,
             final Thunk0 x1,
             final Thunk0 x2,
             final Thunk0 x3,
             final Thunk0 x4,
             final Thunk0 x5) {
        class Thunk0a implements Thunk0 {
            public float value() {
                k.i -= 1 ;
                // In Knuth, the first (int) arg is passed by value.
                // Java restrictions on scope make this difficult
                // and so pass by reference, but clone the argument at this point.
                // Knuth uses "b" as the second argument.
                // But that won't compile (with the Eclipse
                // compiler) and so I use "this".
                return a(new MutInt0(k.i), this, x1, x2, x3, x4) ;
            } } ;
        Thunk0 b = new Thunk0a() ;
        if( k.i <= 0 ) return x4.value() + x5.value() ;
        else return b.value() ;
    }
    
    public static void main(String[] args) {
        class T1 implements Thunk0 {
            public float value() {return 1 ; }  } ;
        class Tminus1 implements Thunk0 {
            public float value() {return -1 ; }  } ;
        class T0 implements Thunk0 {
            public float value() {return 0 ; }  } ;
       Thunk0 t1 = new T1() ;
       Thunk0 t_minus_1 = new Tminus1() ;
       Thunk0 t0 = new T0() ;
       MutInt0 k0 = new MutInt0(10) ;
        
       System.out.println(a(k0, t1, t_minus_1, t_minus_1, t1, t0)) ;
    }
    
}
