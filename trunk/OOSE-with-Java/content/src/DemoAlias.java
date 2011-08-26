public class DemoAlias {

    public static void main(String[] args) {
        MutableInt p = new MutableInt( 42 ) ;
        MutableInt q = p ;
        // q and p are now aliases.
        
        p.setValue( 13 ) ;
        
        System.out.println( p.getValue() ) ;
        System.out.println( q.getValue() ) ;
    }

}
