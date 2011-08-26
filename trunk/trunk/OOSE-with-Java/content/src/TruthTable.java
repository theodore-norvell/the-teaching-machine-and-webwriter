class TruthTable {
    private static void oneLine(boolean a, boolean b) {
        System.out.print( a ) ;
        System.out.print( " and " ) ;
        System.out.print( b ) ;
        System.out.print( " is " ) ; 
        System.out.println( a && b ) ;
    }

    public static void main( String[] args ) {
        for( int i=0 ; i<2 ; i = i+1 )
            for( int j = 0 ; j<2 ; j = j+1 )
                oneLine( i==1, j==1 ) ;
    }
}
