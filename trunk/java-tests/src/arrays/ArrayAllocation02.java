// Test mixed syntax for array allocation
//! run expect output = "287" endl ;

package arrays;

public class ArrayAllocation02 {
    
    public static void main( /*String[] args*/ ) {
        int[][][][][] A ;
        A = new int [2][3][4][][] ;
        int c = 0 ;
        for( int i = 0 ; i < 2 ; ++ i ) 
            for( int j = 0 ; j < 3 ; ++ j )
                for( int k = 0 ; k < 4 ; ++ k ) {
                    A[i][j][k] = new int[4][3] ;
                    for( int l = 0 ; l < 4 ; ++ l ) 
                        for( int m = 0 ; m < 3 ; ++ m ) {
                            A[i][j][k][l][m] = c++ ;
                        }
                }
        System.out.println( A[1][2][3][3][2] ) ;
    }
}
