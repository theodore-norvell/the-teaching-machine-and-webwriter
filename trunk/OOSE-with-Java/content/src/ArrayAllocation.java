public class ArrayAllocation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {/*#TA*/
		int [] a ;
		// Here "a" does not point to any array.
		// The next line creates the array and sets "a" to
		// point to it.
		
		a = new int[10] ;
		// Here "a" points to the just allocated array.
		
		int [] b = a ;
		// Here both "a" and "b" refer to the same array.
		
		// Fill up "a"
		for( int i=0 ; i<10 ; ++i )
			a[i] = (i*i - i)/2 ;
		
		// Print "b"
		for( int i=0 ; i<10 ; ++i )
			System.out.println( b[i] ) ;/*#/TA*/
	}
}
