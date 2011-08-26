//Test initializer syntax
//! run expect output = "12" endl "12" endl "31" endl ;

package arrays;

public class ArrayAllocation03 {
    
    public static void main( /* String[] args */ ) {
        int[]  B = new int[3] ;
        int[][] A = {B, B, B} ;
        A[0][0] = 3 ;
        A[0][1] = 4 ;
        A[0][2] = 5 ;
        System.out.println( A[1][0] + A[1][1] + A[1][2] ) ;
        
        A = new int[][] {B, B, B} ;
        A[0][0] = 3 ;
        A[0][1] = 4 ;
        A[0][2] = 5 ;
        System.out.println( A[1][0] + A[1][1] + A[1][2] ) ;
        
        int[][] T = { {1}, {1,1}, {1,2,1}, {1,3,3,1}, {1,4,6,4,1} } ;
        int sum = 0 ;
        for( int i = 0 ; i < 5 ; ++i )
            for( int j = 0 ; j <= i ; ++j ) 
                sum = sum + T[i][j] ;
           
        System.out.println(sum) ;
    }
}
