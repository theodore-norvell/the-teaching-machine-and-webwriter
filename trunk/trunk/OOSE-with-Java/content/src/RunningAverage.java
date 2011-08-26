class RunningAverage {

    public static void main(String[] args) {
        double[] in = new double[ 5 ] ;
        in[0] = 4.5 ; 
        in[1] = 3.5 ; 
        in[2] = 1.0 ; 
        in[3] = 7.5 ; 
        in[4] = 4.0 ;/*#TA*/
        double[] out = new double[ 5 ] ;
        {   double currentSum = 0.0 ;
            for( int i=0 ; i < 5 ; ++i ) {
                currentSum = currentSum + in[ i ];
                out[ i ] = currentSum / (i+1) ; }
        }/*#/TA*/
    }
}
