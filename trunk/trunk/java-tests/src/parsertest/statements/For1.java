package parsertest.statements;



public class For1 {
    static public void main() {
        {  // E ; E ; E
            int i ;
            i = 0 ;
            for( ; ; ) {
                if( i == 3 ) break ;
                if( i == 1 ) { i = 3 ; continue ; }
                ++i ; }
        }

        { // X ; E ; E
            int i ;
            for(
                 i = 0 ; ; ) {
                if( i == 3 ) break ;
                if( i == 1 ) { i = 3 ; continue ; }
                ++i ; }
        }

        { // D ; E ; E
            f : for(
                     int i = 0 ; ; ) {
                if( i == 3 ) break f;
                if( i == 1 ) { i = 3 ; continue f; }
                ++i ; }
        }

        { // E ; X ; E
            int i ;
            i = 0 ;
            for( ;
                 i!=3 ; ) {
                if( i == 1 ) { i = 3 ; continue ; }
                ++i ; }
        }

        { // X ; X ; E
            int i ;
            f : for(
                     i = 0 ;
                     i!=3 ; ) {
                if( i == 1 ) { i = 3 ; continue ; }
                ++i ; }
        }

        { // D ; X ; E
            for(
                 int i = 0 ;
                 i!=3 ; ) {
                if( i == 1 ) { i = 3 ; continue ; }
                ++i ; }
        }

        { // E ; E ; X
            int i ;
            i = 0 ;
            f : for( ; ;
                     ++i ) {
                if( i == 4 ) break f ;
                if( i == 1 ) { i = 3 ; continue f ; }
            }
        }

        { // X ; E ; X
            int i ;
            f : for(
                     i = 0 ; ;
                     ++i ) {
                if( i == 4 ) break ;
                if( i == 1 ) { i = 3 ; continue ; }
            }
        }

        { // D ; E ; X
            for(
                  int i = 0 ; ;
                  ++i ) {
                if( i == 4 ) break ;
                if( i == 1 ) { i = 3 ; continue ; }
            }
        }

        { // E ; X ; E
            int i ;
            i = 0 ;
            f : for( ;
                     i!=4 ;
                     ++i ) {
                if( i == 1 ) { i = 3 ; continue f ; }
            }
        }

        { // X ; X ; X
            int i ;
            f : for(
                     i = 0 ;
                     i!=4 ;
                     ++i ) {
                if( i == 1 ) { i = 3 ; continue ; }
            }
        }

        { // D ; X ; X
            for(
                 int i = 0 ;
                 i!=4 ;
                 ++i ) {
                if( i == 1 ) { i = 3 ; continue ; }
            }
        }

        { // MX ; X ; MX
            int i, k ;
            for( i=0,
                 k=4 ;
                 i<k ;
                 ++i,
                 --k ) ;
        }

        { // E ; X ; MX
            int i, k ;
            i = 0 ; k = 4 ;
            for( ;
                 i<k ;
                 ++i,
                 --k ) ;
        }

        { // MX ; X ; E
            int i, k ;
            for( i=0,
                 k=4 ;
                 i<k ;  ) {
                ++i;
                --k ; }
        }

        { // MD ; X ; MX
            f : for( int i = 0,
                     k=4 ;
                     i<k ;
                     ++i,
                     --k ) ;
        }
    }
}