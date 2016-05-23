package tm.displayEngine;

import java.awt.Graphics2D ;

import telford.common.Canvas ;
import tm.portableDisplays.PortableDisplay ;

public class SwingDisplay extends DisplayAdapter {

    private PortableDisplay displayer ;
    Canvas canvas = new telford.common.Canvas() ;

    public SwingDisplay(DisplayManager dm, String configId, PortableDisplay displayer) {
        super( dm, configId ) ;
        this.displayer = displayer ;
        add( (javax.swing.JComponent) canvas.getPeer().getRepresentative() ) ;
        // TODO worry about buttons later.
    }

    @Override
    public void drawArea(Graphics2D screen) {
        displayer.drawArea( canvas, commandProcessor ) ;
    }
    
    @Override 
    public void refresh() {
        super.refresh(); 
        displayer.refresh( commandProcessor ) ;
    }

}
