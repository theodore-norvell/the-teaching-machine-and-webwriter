package tm.interfaces;

public class StateFormatter {
    static final char newline = '\n' ;
    static public void formatState( StateInterface state, StringBuffer b, String indent) {
        b.append( indent ) ;
        b.append( "State " ) ;
        b.append( System.identityHashCode(state)  ) ;
        b.append( newline ) ;
        formatStore( state.getStore(), b, indent+"   |" ) ;
    }
    static public void formatStore( StoreInterface store, StringBuffer b, String indent) {
        b.append( indent ) ;
        b.append( "Store object " ) ;
        b.append( newline ) ;
        b.append(  indent+"  Stack: " ) ; b.append( newline ) ;
        formatDatum( store.getStack(), b, indent+"   |" ) ;
        b.append(  indent+"  Static: " ) ; b.append( newline ) ;
        formatDatum( store.getStatic(), b, indent+"   |" ) ;
        b.append(  indent+"  Heap: " ) ; b.append( newline ) ;
        formatDatum( store.getHeap(), b, indent+"   |" ) ;
        b.append(  indent+"  Scratch: " ) ; b.append( newline ) ;
        formatDatum( store.getScratch(), b, indent+"   |" ) ;
    }
    private static void formatDatum(Datum d, StringBuffer b,
            String indent) {
        b.append( indent ) ;
        b.append( "Datum " ) ;
        b.append( System.identityHashCode(d) ) ;
        b.append( newline ) ;
        b.append( indent ) ;
        b.append( "  Serial No:" ) ; b.append( d.getSerialNumber() ) ;
        b.append( newline ) ;
        b.append( indent ) ;
        b.append( "  Parent:" ) ; b.append( d.getParent()==null? "null" : System.identityHashCode(d.getParent())) ;
        b.append( newline ) ;
        b.append( indent ) ;
        b.append( "  Name:" ) ; b.append( d.getName() ) ;
        b.append( newline ) ;
        b.append( indent ) ;
        b.append( "  Value:" ) ; b.append( d.getValueString() ) ;
        b.append( newline ) ;
        b.append( indent ) ;
        b.append( "  Type:" ) ; b.append( d.getTypeString() ) ;
        b.append( newline ) ;
        int sz = d.getNumChildren() ;
        if( sz > 0 ) {
            b.append( indent ) ;
            b.append( "  "+sz+(sz==1 ? " child:" : " children:") ) ; 
            b.append( newline ) ;
            for( int i = 0 ; i<sz ; ++i ) {
                formatDatum( d.getChildAt( i ), b, indent+"   |" ) ; } }
    }
    
    static public void formatConsoleLines(StateInterface state, StringBuffer b, String indent){
    	b.append( indent );
    	if(state.getNumConsoleLines() != 0){
    		for(int i = 0; i<state.getNumConsoleLines(); ++i){
    			b.append("Line " + i + " is");
    			b.append( newline ) ;
    			b.append(state.getConsoleLine(i));
    		}
    	}
    	else{
    		b.append("No lines in console.");
    	}
    	b.append( newline ) ;
    	b.append( indent );
    	b.append("On client side");
    }
    
}
