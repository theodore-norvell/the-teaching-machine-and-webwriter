/*#HA*/
/*#HB*/
public class DemoSort {
/*#DA*/
    private static void sort ( int[] a ) {
        // Inv: a is a premutation of its initial value
        // Inv: a[0, 1, ..., i-1] is sorted.
        // Inv: a[k0] <= a[k1], for all k0 in {0, 1, ..., i-1} and
        //                              k1 in {i, i+1, ..., a.length-1}
        for( int i=0 ; i < a.length - 1 ; ++i ) {
            // Set m such that i <= m && m < a.length &&
            //  (forall k in {i, i+1, ..., a.length-1} : a[m] <= a[k])
                int m = i ;
                // Inv: (forall k in {i, i+1, ..., j-1} : a[m] <= a[k])
                for( int j = i+1 ; j < a.length ; ++j ) {
                    if( a[j] < a[m] ) m = j ; }
            if( m != i ) {
                int t = a[i] ; a[i] = a[m] ; a[m] = t ; }
        }
    }
/*#HA*/
    
    public static void print( int[] a) {
        for( int i = 0 ; i < a.length ; ++i ) {
            System.out.print( a[i] ) ;
            System.out.print( " " ) ; }
        System.out.println() ;
    }
/*#DB*/    
    public static void main(String[] args) {
        int[] b = new int[4] ;
        b[0] = 12 ; b[1] = 8 ; b[2] = 14 ; b[3] = 6 ;
        sort( b ) ;
        print( b ) ;
        
        int[] c = new int[3] ;
        c[0] = 3 ; c[1] = 5 ; c[2] = 1 ;
        sort( c ) ;
        print( c ) ;
    }
/*#HB*/
}
