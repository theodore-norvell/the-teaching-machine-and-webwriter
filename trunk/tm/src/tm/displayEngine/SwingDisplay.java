package tm.displayEngine;

import java.awt.BorderLayout ;
import java.awt.Graphics2D ;

import javax.swing.JScrollPane ;

import tm.portableDisplays.PortableDisplayer;

public class SwingDisplay extends DisplayAdapterSwing {

    public PortableDisplayer displayer ;

	public SwingDisplay(DisplayManager dm, String configId, PortableDisplayer displayer) {
        super( (javax.swing.JComponent)displayer.getPeer().getRepresentative(),
               new JScrollPane(), // TODO. Get this from Telford
               dm,
               configId ) ;
        this.displayer = displayer ;
    }
    
    @Override 
    public void refresh() {
        displayer.refresh( ) ;
        super.refresh(); 
    }
}
