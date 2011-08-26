public class ReturnedValue {
/*#TA*/
	private static double value( double p, double rate, int periods ) {
		double w = 1.0 ;
		for( int i=0 ; i<periods ; ++i ) w = w * (1+rate) ; 
		return p * w ;
	}
	
	public static void main(String[] args) {
		double principle = 1000.0 ;
		double interestRate = 0.05 ;
		for( int i = 0 ; i <= 12 ; ++i ) {
			double futureValue = value( principle, interestRate, i) ;
			System.out.print( i ) ;
			System.out.print( ": ") ;
			System.out.println( futureValue ) ; }
	}/*#/TA*/
}
