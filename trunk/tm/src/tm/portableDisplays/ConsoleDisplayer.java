package tm.portableDisplays;
import telford.common.FontMetrics;
import telford.common.Graphics;
import tm.interfaces.StateInterface;

public class ConsoleDisplayer extends PortableDisplayer{
	
	private static final char MARKER_BOUND = StateInterface.INPUT_MARK;
    private final static int LEFT_MARGIN = 10;
    private final static int TOP_MARGIN = 10;
    private final static int TABSPACE = 4;
    public int delta_y = 12; //The height of each line, which consists of lineHeight and line padding
	
	public ConsoleDisplayer(StateInterface model, PortableContextInterface context) {
		super(model, context);		
	}

	
	@Override
	public void refresh() {
	}

// =================================================================
// Graphics Methods
// =================================================================

    @Override
    public void paintComponent(Graphics g){
    	//if( model == null ) return ;
        context.log(">> ConsoleDisplayer.paintComponent.") ;
        g.setFont(context.getCodeFont());
        FontMetrics fm = g.getFontMetrics(g.getFont());
        delta_y = fm.getHeight() + 1;
        int baseLine = TOP_MARGIN;

        int numLines = model.getNumConsoleLines() ;
        
        for (int i = 0; i< numLines; i++) {
            baseLine += fm.getAscent();
            String theLine = model.getConsoleLine(i);
            if(theLine != null)
                drawLine(expandTabs(theLine),LEFT_MARGIN,baseLine, g, fm);
        }
        context.log("<< ConsoleDisplayer.paintComponent.") ;
    }

// Draws a single line, taking mode changes into account
    public void drawLine(String theLine, int x, int y, Graphics screen, FontMetrics fm) {
    // Must convert expression to array of characters as drawChars can only work with
    // char arrays
        if (theLine.length() > 0) {
            char[] tempArray = theLine.toCharArray();
            for( int i = 0; i < theLine.length(); ++ i ) {
                char c = tempArray[i];
 
                if ( c >= MARKER_BOUND)
                    screen.setColor(c==StateInterface.INPUT_MARK ? 0xFF0000 : 0x000000 );
                else {
                    screen.drawString(tempArray, i, 1, x, y);             
                    x += fm.stringWidth( tempArray, i, 1 ) ;
                }
            }
        }
    }
    

    public String expandTabs( String theLine ) {
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
    
    /*
     * Method for other platforms to get the number of console lines
     */
    public int getNumConsoleLines(){
    	return model.getNumConsoleLines();
    }
    
    /*
     * Method for other platforms to get console lines
     */
    public String getConsoleLine(int i){
    	return model.getConsoleLine(i);
    }
    
    public int getDelta_y(){
    	return delta_y;
    }
    
}
