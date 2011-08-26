// Test allocation and access to a single dimensional array
//! run expect output = "15" endl "15" endl "15" endl ;
package arrays;

public class ArrayAllocation00 {
    public static void main( /*String[] args*/ ) {
        int [] A = new int[5] ;
        for( int i = 0 ; i < 5 ; ++i ) {
            A[i] = A[i] + i+1 ; }
        runningSum( A ) ;

        int B[] = new int[5] ;
        for( int i = 0 ; i < 5 ; ++i ) {
            B[i] = B[i] + i+1 ; }
        runningSum( B ) ;
        
        A = new int[5] ;
        for( int i = 0 ; i < 5 ; ++i ) {
            A[i] = A[i] + i+1 ; }
        runningSum( A ) ;
    }
    

    static void runningSum( int [] X ) {
        for ( int i = 0 ; i < 4 ; ++ i ) {
            X[i+1] = X[i+1] +  X[i] ; }
        System.out.println( X[4] ) ;
    }
}
