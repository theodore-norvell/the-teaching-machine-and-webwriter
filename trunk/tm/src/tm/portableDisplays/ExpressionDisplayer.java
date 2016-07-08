package tm.portableDisplays;

import java.util.Vector;

import telford.common.FontMetrics;
import telford.common.Graphics ;
import tm.interfaces.StateInterface ;

public class ExpressionDisplayer extends PortableDisplayer {
    public ExpressionDisplayer( StateInterface model, PortableContextInterface context ) {
        super( model, context ) ;
    }
    
    private static final int LEFTMARGIN = 4;
    private static final int TOPMARGIN = 2;

    public static final char MARKER1 = '\uffff';    
    public static final char MARKER2 = '\ufffe';    
    public static final char MARKER3 = '\ufffd';    
    public static final char MARKER4 = '\ufffc';
    public static final char ENDMARKER = '\ufffb';
    

    protected String theExpression = "";

    @Override
    public void refresh() {
        theExpression = model.getExpression();
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	drawArea(g);
    }
    
    public void drawArea(Graphics g){
		int advance = LEFTMARGIN;
		g.setFont(context.getDisplayFont());

		if (theExpression.length() > 0) {
			char tempArray[] = theExpression.toCharArray();
			FontMetrics fm = g.getFontMetrics(g.getFont());
			int currWidth = 0;
			int baseline = TOPMARGIN + fm.getAscent();

			g.setColor(0x000000);//black
			int currColor = 0x000000 ;
			boolean currUnderline = false ;
			Vector<Object> attrStack = new Vector<Object>() ;
			for( int i = 0, sz = theExpression.length() ; i < sz ; ++ i ) {
				char c = tempArray[i];		// Next character
				switch (c) {
				case MARKER1:
					attrStack.addElement("red");
					currColor = 0xFF0000;
					g.setColor(currColor);
					break;
				case MARKER2:
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = false;
					break;
				case MARKER3:
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = true;
					break;
				case MARKER4:
					attrStack.addElement("blue");
					currColor = 0x0000FF;
					g.setColor(currColor);
					break;
				case ENDMARKER:
					Object temp = attrStack.elementAt(attrStack.size() - 1);
					if (temp instanceof String) {
						currColor = temp.equals("red") ? 0xFF0000 : 0x0000FF;
						g.setColor(currColor);
					} else {
						currUnderline = ((Boolean) temp).booleanValue();
					}
					attrStack.removeElementAt(attrStack.size() - 1);
					break;
					default:
					    g.drawString(String.valueOf(tempArray[i]), advance, baseline);
					    currWidth = fm.stringWidth(String.valueOf(tempArray[i]));
					    if( currUnderline ) {
					        g.setColor( 0x000000 ) ;
					        g.fillRect( advance, baseline+2, currWidth, +3);
					        g.setColor( currColor ) ; }
					    advance += currWidth;
				}  // Switch
			}  // For loop
		}   // End if we have an expression
	}   // End drawArea
    
    public int calFocusPosition(){
		int position = 0;
		return position;
	}
}
