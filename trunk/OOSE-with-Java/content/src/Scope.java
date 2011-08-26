class Scope {
    static public void main(String [] args ) {
        int i = 5 ;
        {
           int j = 6 ;
           System.out.println( i + j ) ;
        }
        
        int k = 7 ;
        {
           int j = 7 ;
           System.out.println( i + j + k ) ;
        }
    }
}
        