package tm.displayEngine;

import java.awt.BorderLayout ;
import java.awt.Graphics2D ;

import tm.portableDisplays.PortableDisplayer;

public class SwingDisplay extends DisplayAdapterSwing {

    public PortableDisplayer displayer ;

	public SwingDisplay(DisplayManager dm, String configId, PortableDisplayer displayer) {
        super( dm, configId ) ;
        this.displayer = displayer ;
        this.setLayout( new BorderLayout() ); // Is this needed?
        add( (javax.swing.JComponent) displayer.getPeer().getRepresentative(), BorderLayout.CENTER ) ;
    }

    @Override
    public void drawArea(Graphics2D screen) {
        // Draw area doesn't need to do anything.  Here is why:
        // drawArea is called only when the component is painted. 
        // When the component is painted, the displayer's peer will be painted.
        // So whatever functionality would go here in the classic displays should
        // be moved to the displayer's paintComponent method in the Telford system.
    }
    
    @Override 
    public void refresh() {
        super.refresh(); 
        displayer.refresh( ) ;
    }
}
