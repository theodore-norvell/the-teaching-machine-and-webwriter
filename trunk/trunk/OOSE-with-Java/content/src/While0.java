class While0 {

    public static void main(String[] args) {
        int n = 5 ;/*#TA*/
        int t = 0 ;
        {   int i = n ;
            // Invariant: t == (Sum j in {i+1, i+2, ..., n}: j)
            while( i > 0 ) {
                t = t + i ;
                i = i - 1 ; }
        }
/*#/TA*/
        System.out.println( t ) ;
    }
}
