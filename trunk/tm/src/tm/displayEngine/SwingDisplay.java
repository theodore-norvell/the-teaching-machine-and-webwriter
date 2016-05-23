package tm.displayEngine;

import java.awt.BorderLayout ;
import java.awt.Graphics2D ;

import telford.common.Canvas ;
import tm.portableDisplays.PortableDisplayer ;

public class SwingDisplay extends DisplayAdapter {

    private PortableDisplayer displayer ;

    public SwingDisplay(DisplayManager dm, String configId, PortableDisplayer displayer) {
        super( dm, configId ) ;
        this.displayer = displayer ;
        this.setLayout( new BorderLayout() ); // Is this needed?
        add( (javax.swing.JComponent) displayer.getPeer().getRepresentative(), BorderLayout.CENTER ) ;
    }

    @Override
    public void drawArea(Graphics2D screen) {
    }
    
    @Override 
    public void refresh() {
        super.refresh(); 
        displayer.refresh( ) ;
    }

}
