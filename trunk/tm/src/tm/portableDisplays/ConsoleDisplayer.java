package tm.portableDisplays;

import java.awt.Dimension;

import telford.common.Color;
import telford.common.FontMetrics;
import telford.common.Graphics;
import tm.interfaces.StateInterface;

public class ConsoleDisplayer extends PortableDisplayer{
	
	private static final char MARKER_BOUND = StateInterface.INPUT_MARK;
	private int advances[];
    private final static int LEFT_MARGIN = 10;
    private final static int TOP_MARGIN = 10;
    private final static int TABSPACE = 4;
    private int numLines = 0;
	
	public ConsoleDisplayer(StateInterface model, PortableContextInterface context) {
		super(model, context);		
	}

	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	
    private int stringWidth(String theLine, Graphics screen){
        int theWidth = 0;
        if (theLine.length() > 0) {
            String expanded = expandTabs(theLine);
            for( int i = 0 ; i < expanded.length(); ++ i ) {
                char c = expanded.charAt(i);
                
                theWidth += advances[c]; //needs to be modified
                /* 
                if ( c >= MARKER_BOUND)
                	screen.setColor(c==StateInterface.INPUT_MARK ? Color.getRed() : Color.getBlack());                	              
                else
                    theWidth += advances[c]; */
            }
        }
        return theWidth;
    }
// =================================================================
// Graphics Methods
// =================================================================

    public void paintComponent(Graphics g){
    	//if( model == null ) return ;
        g.setFont(context.getCodeFont());
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int baseLine = TOP_MARGIN;
        String theLine = null;

    //	if (getLastShowing() > 0)
            for (int i = 0; i< numLines; i++) {
                baseLine += fm.getAscent();
                theLine = model.getConsoleLine(i);
                if(theLine != null)
                    drawLine(expandTabs(theLine),LEFT_MARGIN,baseLine, g);
            }
    }

// Draws a single line, taking mode changes into account
    private void drawLine(String theLine, int x, int y, Graphics screen) {
    // Must convert expression to array of characters as drawChars can only work with
    // char arrays
        if (theLine.length() > 0) {
            char[] tempArray = theLine.toCharArray();
            for( int i = 0; i < theLine.length(); ++ i ) {
                char c = tempArray[i];
                
                //needs to be modified
                screen.drawString(tempArray, i, 1, x, y);             
                x += advances[c];
                /*
                if ( c >= MARKER_BOUND)
                    screen.setColor(c==StateInterface.INPUT_MARK ? Color.getRed() : Color.getBlack());
                else {
                    screen.drawString(tempArray, i, 1, x, y);
                   
                    x += advances[c]; 
                } */
            }
        }
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
