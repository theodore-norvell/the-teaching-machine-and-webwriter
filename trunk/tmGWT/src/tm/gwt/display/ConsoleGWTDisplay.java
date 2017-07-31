package tm.gwt.display;

import tm.gwt.jsInterface.GWTSuperTMFile;
import tm.interfaces.StateInterface;
import tm.portableDisplays.ConsoleDisplayer;
import tm.portableDisplays.PortableContextInterface;

public class ConsoleGWTDisplay extends DisplayAdapter {

	private GWTSuperTMFile theFile;
	StateInterface evaluator;
	PortableContextInterface context = new GWTContext();
	ConsoleDisplayer consoleDisplayer;
	private int advances[];
	private static final char MARKER_BOUND = StateInterface.INPUT_MARK;
    private final static int TABSPACE = 4;
	
	public ConsoleGWTDisplay(StateInterface e, PortableContextInterface context) {
		super(new ConsoleDisplayer(e,context), "ConsoleDisplayPanel", "Console", 300, 100);
		this.evaluator = e;
		this.context = context;			
	}
	
	public void refresh(){		
		
		int n = evaluator.getNumConsoleLines();
		int numLines = 0;
        if (n != numLines) {
            int width = 0;
            int theWidth = 0;
            String theLine = null;
            setScale(1,12);
            
            for (int i = 0; i < n; i++) {
                theLine = evaluator.getConsoleLine(i);
                if(theLine != null) {
                    theWidth = stringWidth(theLine);
                    if (theWidth > width) width = theWidth;
                }
            }
            
            numLines = n;
                       
        }
        
        super.refresh();
        
	}
	
    private int stringWidth(String theLine){
        int theWidth = 0;
        if (theLine.length() > 0) {
            String expanded = expandTabs(theLine);
            for( int i = 0 ; i < expanded.length(); ++ i ) {
                char c = expanded.charAt(i);
                /*
                if ( c >= MARKER_BOUND)
                    g.setColor(c==StateInterface.INPUT_MARK ? 0xFF0000: 0x000000);
                else
                */
                    theWidth += advances[c];
            }
        }
        return theWidth;
    }
    
    private String expandTabs( String theLine ) {
        int column = 0 ;
        StringBuffer buf = new StringBuffer() ;
        for( int i=0 , sz = theLine.length() ; i < sz ; ++i ) {
            char c = theLine.charAt(i) ;
            if( c >= MARKER_BOUND ) {
                buf.append(c) ; }
            else if( c == '\t' ) {
                column = (column / TABSPACE  + 1) * TABSPACE ;
                while( buf.length() < column ) buf.append(' ') ; }
            else {
                column += 1 ;
                buf.append(c) ; } }
        return buf.toString() ; }
	

}
