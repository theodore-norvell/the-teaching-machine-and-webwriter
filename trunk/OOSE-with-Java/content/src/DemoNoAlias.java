public class DemoNoAlias {

    public static void main(String[] args) {
        MutableInt p = new MutableInt( 42 ) ;
        MutableInt q = new MutableInt( 42 ) ;
        
        p.setValue( 13 ) ;
        
        System.out.println( p.getValue() ) ;
        System.out.println( q.getValue() ) ;
    }

}
