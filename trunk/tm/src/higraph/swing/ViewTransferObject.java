/*
 * Created on 2010-03-02 by Theodore S. Norvell. 
 */
package higraph.swing;

import higraph.view.ComponentView;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ViewTransferObject implements Transferable {
    
    static public final DataFlavor theViewDataFlavor
    = new DataFlavor(ComponentView.class, "ComponentView DataFlavor") ;

    private final  ComponentView view ;
    
    public ViewTransferObject( ComponentView view ) {
        this.view = view ;
    }
    
    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if( flavor.equals( theViewDataFlavor )) 
            return view ;
        if( flavor.equals( DataFlavor.stringFlavor )) 
            return view.toString() ;
        else throw new UnsupportedFlavorException(flavor) ;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        // For demonstration, support 2 flavors
        return new DataFlavor[]{ DataFlavor.stringFlavor, theViewDataFlavor } ; 
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {

        // For demonstration, support 2 flavors
        if( flavor.equals(DataFlavor.stringFlavor) ) 
            return true ;
        else if( flavor.equals( theViewDataFlavor )) 
            return true ;
        else return false ;
    }
}