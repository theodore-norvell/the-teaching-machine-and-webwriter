class Swap {
    /*#T A*/
    public static void swap( int x, int y ) {
        int t = x ;
        x = y ;
        y = t ;
    }/*#/T A*/
    
    public static void main( String[] args ) {
        int a = 2 ;
        int b = 5 ;
        
        swap( a, b ) ;
        
        System.out.println( a ) ;
        System.out.println( b ) ;
    }
}
        