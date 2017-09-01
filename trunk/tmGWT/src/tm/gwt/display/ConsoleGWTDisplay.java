package tm.gwt.display;

import telford.common.FontMetrics;
import tm.gwt.jsInterface.GWTSuperTMFile;
import tm.gwt.telford.GraphicsGWT;
import tm.interfaces.StateInterface;
import tm.portableDisplays.ConsoleDisplayer;
import tm.portableDisplays.PortableContextInterface;

public class ConsoleGWTDisplay extends DisplayAdapter {

	private GWTSuperTMFile theFile;
	private static final char MARKER_BOUND = StateInterface.INPUT_MARK;
    private final static int LEFT_MARGIN = 10;
    private final static int TOP_MARGIN = 10;
    private final static int TABSPACE = 4;
    
	StateInterface evaluator;
	PortableContextInterface context = new GWTContext();
	ConsoleDisplayer consoleDisplayer;
	//private int advances[];

	
	public ConsoleGWTDisplay(StateInterface e, PortableContextInterface context) {
		super(new ConsoleDisplayer(e,context), "ConsoleDisplayPanel", "Console", 300, 75);
		this.evaluator = e;
		this.context = context;	
		
	    context.getAsserter().check( this.displayer instanceof ConsoleDisplayer ) ;
	    this.consoleDisplayer = (ConsoleDisplayer)this.displayer;
	        
		myWorkPane.setStyleName("tm-smallScrollPanel");
	}
	
	
	public void refresh(){		
	    com.google.gwt.core.client.GWT.log( ">> ConsoleGWTDisplay.refresh") ;
//		int n= consoleDisplayer.getNumConsoleLines();
//		int numLines = 0;
//        if (n != numLines) {
//            int width = 0;
//            int theWidth = 0;
//            String theLine = null;    
//            for (int i = 0; i <= n; i++) {
//                theLine = consoleDisplayer.getConsoleLine(i);
//                if(theLine != null) {
//                    theWidth = stringWidth(theLine);
//                    if (theWidth > width) width = theWidth;
//                }
//            }   
//            setScale(width,12); 
//        }
        super.refresh();

        com.google.gwt.core.client.GWT.log( "<< ConsoleGWTDisplay.refresh") ;
	}
    
	
//    private int stringWidth(String theLine){
//        int theWidth = 0;
//        if (theLine.length() > 0) {
//            String expanded = expandTabs(theLine);
//            for( int i = 0 ; i < expanded.length(); ++ i ) {
//                char c = expanded.charAt(i);
//                    theWidth += advances[c];
//            }
//        }
//        return theWidth;
//    }
//
//    private String expandTabs( String theLine ) {
//        int column = 0 ;
//        StringBuffer buf = new StringBuffer() ;
//        for( int i=0 , sz = theLine.length() ; i < sz ; ++i ) {
//            char c = theLine.charAt(i) ;
//            if( c >= MARKER_BOUND ) {
//                buf.append(c) ; }
//            else if( c == '\t' ) {
//                column = (column / TABSPACE  + 1) * TABSPACE ;
//                while( buf.length() < column ) buf.append(' ') ; }
//            else {
//                column += 1 ;
//                buf.append(c) ; } }
//        return buf.toString() ; } 
//
	

}
