public class ReturnedArray {
/*#TA*/
	private static int[] reverse( int[] x) {
		int [] y = new int[ x.length ] ;
		for( int i=0 ; i<x.length ; ++i ) 
			y[i] = x[ x.length - 1 - i ] ;
		return y ;
	}

	public static void main(String[] args) {
		final int size = 5 ;
		int [] a = new int[ size ] ;
		for( int i=0 ; i<size ; ++i)
			a[i] = i*11 % 7 ;
		int [] b = reverse( a ) ;
		for( int i=0 ; i<size ; ++i)
			System.out.println(b[i]) ;
	}/*#/TA*/

}
