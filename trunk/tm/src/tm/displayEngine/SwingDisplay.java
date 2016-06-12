package tm.displayEngine;

import java.awt.BorderLayout ;
import java.awt.Graphics2D ;

import tm.configuration.Configuration;
import tm.portableDisplays.PortableDisplayer;

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
    @Override 
    public void notifyOfSave(Configuration config) {
    	super.notifyOfSave(config);
    	displayer.notifyOfSave(config);
    	
    }
    @Override 
    public void notifyOfLoad(Configuration config) {
    	super.notifyOfLoad(config);
    	displayer.notifyOfLoad(config);
    	
    }
}
