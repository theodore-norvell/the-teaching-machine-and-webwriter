
public class DemoMutableIntSwap {

    
    private static void swap( MutableInt p, MutableInt q ) {
        int temp = p.getValue() ;
        p.setValue( q.getValue() ) ;
        q.setValue( temp ) ;
    }
    
    public static void main(String[] args) {
        MutableInt a = new MutableInt( 42 ) ;
        MutableInt b = new MutableInt( 13 ) ;
        
        swap( a, b) ;
        
        System.out.println( a.getValue() ) ;
        System.out.println( b.getValue() ) ;

    }

}
