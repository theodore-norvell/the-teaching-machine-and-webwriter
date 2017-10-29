package tm.displayEngine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import tm.interfaces.StateInterface;
import tm.portableDisplays.ConsoleDisplayer;

public class ConsoleSwingDisplay extends SwingDisplay{
	
	//Printing modes
    private static final char MARKER_BOUND = StateInterface.INPUT_MARK;

    private final static int LEFT_MARGIN = 10;
    private final static int TABSPACE = 4;

    private int numLines = 0;
    private int advances[];
    
	private ConsoleDisplayer consoleDisplayer;

    public ConsoleSwingDisplay(DisplayManager dm, String configId){
    	super(dm, configId, new ConsoleDisplayer(dm.getCommandProcessor(), dm.getPortableContext()));
		dm.getPortableContext().getAsserter().check( this.displayer instanceof ConsoleDisplayer ) ;
		this.consoleDisplayer = (ConsoleDisplayer)this.displayer;
    }



// =================================================================
// Resource Interface Methods
// =================================================================

    @Override
    public void refresh(){
        // Must be connected.
        if( commandProcessor == null ) return ;
        Graphics g = myComponent.getGraphics();
        if (g == null) return;
        g.setFont(context.getCodeFont());
        FontMetrics fm = g.getFontMetrics();
        advances = fm.getWidths();


        int n = commandProcessor.getNumConsoleLines();
        if (n != numLines) {
            int width = 0;
            int theWidth = 0;
            String theLine = null;

            setScale( 1, fm.getHeight() + 2 );

        // Find the rightMost edge of the longest line and use it to set
        // world co-ordinates
            for (int i = 0; i < n; i++) {
                theLine = commandProcessor.getConsoleLine(i);
                if(theLine != null) {
                    theWidth = stringWidth(theLine, g);
                    if (theWidth > width) width = theWidth;
                }
            }
            setPreferredSize (new Dimension(width+LEFT_MARGIN,n*getVScale()));
            numLines = n;
        }
        super.refresh();
        g.dispose();
    }

//	 Replaces fontMetrics.stringWidth - takes colour changes into account
    private int stringWidth(String theLine, Graphics screen){
        int theWidth = 0;
        if (theLine.length() > 0) {
            String expanded = expandTabs(theLine);
            for( int i = 0 ; i < expanded.length(); ++ i ) {
                char c = expanded.charAt(i);
                if ( c >= MARKER_BOUND)
                    screen.setColor(c==StateInterface.INPUT_MARK ? Color.red : Color.black);
                else
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
