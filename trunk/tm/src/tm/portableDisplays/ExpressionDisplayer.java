package tm.portableDisplays;

import telford.common.Graphics ;
import tm.interfaces.DisplayContextInterface ;
import tm.interfaces.EvaluatorInterface ;

public class ExpressionDisplayer extends PortableDisplayer {

    public ExpressionDisplayer( EvaluatorInterface model, DisplayContextInterface context ) {
        super( model, context ) ;
    }

    private static final int LEFTMARGIN = 4;
    private static final int TOPMARGIN = 2;

    public static final char MARKER1 = '\uffff';    
    public static final char MARKER2 = '\ufffe';    
    public static final char MARKER3 = '\ufffd';    
    public static final char MARKER4 = '\ufffc';
    public static final char ENDMARKER = '\ufffb';
    

    String theExpression = "";

    @Override
    public void refresh() {
        theExpression = model.getExpression();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        int w= this.getWidth() ;
        int h= this.getHeight() ;
        g.setColor( 0x00FFFFFF ) ;
        g.fillRect( 0, 0, w, h );
        int baseline = TOPMARGIN + 16 ; //TODO use the font's height
        g.setColor( 0 ) ;
        g.drawString( "Hello", LEFTMARGIN, baseline ) ;
    }

}
