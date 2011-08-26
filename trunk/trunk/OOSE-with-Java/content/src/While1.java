import java.util.Scanner ;

class While1 {

    public static void main(String[] args) {/*#TA*/
        int count = 0 ;
        {
            Scanner scanner = new Scanner( System.in ) ;
            while( scanner.hasNext() ) {
                count = count + 1 ;
                scanner.next() ; }
        }
/*#/TA*/
        System.out.println(count) ;
    }
}
