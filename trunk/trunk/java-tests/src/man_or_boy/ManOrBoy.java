//! Run. Expect output is "-67.0" endl.
/*
 * Created on 19-Sep-07 by Theodore S. Norvell. 
 * See Donald Knuth (Jul 1964). Man or boy?, Algol Bullitin, 17.2.4, 1964.
 * http://archive.computerhistory.org/resources/text/algol/algol_bulletin/A17/P24.HTM
 */
package man_or_boy;

interface Thunk {
    float value() ;
}

class MutInt {
    public int i ;
    
    public MutInt(int i0) {
        i = i0 ;
    }
    
}

public class ManOrBoy {
    static float a( final MutInt k,
             final Thunk x1,
             final Thunk x2,
             final Thunk x3,
             final Thunk x4,
             final Thunk x5) {
        final Thunk b = new Thunk() {
            public float value() {
                k.i -= 1 ;
                // In Knuth, the first (int) arg is passed by value.
                // Java restrictions on scope make this difficult
                // and so pass by reference, but clone the argument at this point.
                // Knuth uses "b" as the second argument.
                // But that won't compile (with the Eclipse
                // compiler) and so I use "this".
                return a(new MutInt(k.i), this, x1, x2, x3, x4) ;
            } } ;
        if( k.i <= 0 ) return x4.value() + x5.value() ;
        else return b.value() ;
    }
    
    public static void main(String[] args) {
        Thunk t1 = new Thunk() { public float value() {return 1 ; }  } ;
        Thunk t_minus_1 = new Thunk() { public float value() {return -1 ; }  } ;
        Thunk t0 = new Thunk() { public float value() {return 0 ; }  } ;
        MutInt k0 = new MutInt(10) ;
        
        System.out.println(a(k0, t1, t_minus_1, t_minus_1, t1, t0)) ;
    }
    
}
