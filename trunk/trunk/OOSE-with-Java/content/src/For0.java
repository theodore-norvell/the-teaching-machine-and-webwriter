class For0 {

    public static void main(String[] args) {
        int n = 5 ;/*#TA*/
        int t = 0 ;
        // Invariant: t == (Sum j in {i+1, i+2, ..., n}: j)
        for( int i = n ; i > 0 ; i = i-1 )
            t = t + i ;
/*#/TA*/
        System.out.println( t ) ;
    }
}
