class BigNatTest {
    

    public static void main( String[] args ) {
        BigNat nines = new BigNat( 999999999 ) ;
        System.out.println( nines.toString() ) ;
        BigNat two = new BigNat( 2 ) ;
        System.out.println( two.toString() ) ;
        
        BigNat result = nines.add( two ) ;
        System.out.println( result.toString() ) ;
        
        result = nines.multiply( two ) ;
        System.out.println( result.toString() ) ;
        
        result = nines.multiply( nines ) ;
        result = result.multiply( nines ) ;
        System.out.println( result.toString() ) ;
    }
}