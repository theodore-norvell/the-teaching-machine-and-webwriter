/**
 * VisreedNodeTransferObject.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import higraph.swing.ViewTransferObject;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import visreed.extension.regex.swing.RegexJList;
import visreed.model.VisreedNode;

/**
 * VisreedNodeTransferObject implements an transferable object based on 
 * RegexNodes. The object is transferred from the tool bar ({@link RegexJList}) 
 * to one of the diagram containers (usually a {@link VisreedJComponent})
 * @author Xiaoyu Guo
 */
public class VisreedNodeTransferObject implements Transferable {
    public static final DataFlavor theNodeDataFlavor
    = new DataFlavor(VisreedNode.class, "VisreedNode DataFlavor") ;
    
    private static final DataFlavor[] SUPPORTED_DATA_FLAVORS = new DataFlavor[]{
        theNodeDataFlavor, DataFlavor.stringFlavor
    };
    
    private final VisreedNode node;
    
    public VisreedNodeTransferObject(VisreedNode node){
        this.node = node;
    }
    
    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_DATA_FLAVORS; 
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        boolean supported = false;
        for(int i = 0; i < SUPPORTED_DATA_FLAVORS.length; i++){
            if(flavor.equals(SUPPORTED_DATA_FLAVORS[i])){
                supported = true;
                break;
            }
        }
        return supported;
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public Object getTransferData(DataFlavor flavor) 
            throws UnsupportedFlavorException, IOException {
        if( flavor.equals( theNodeDataFlavor )) {
            return node ;
        } else if (flavor.equals(ViewTransferObject.theViewDataFlavor)){
            // TODO get a ComponentView from the node
            assert false;
            return null;
        } else if( flavor.equals( DataFlavor.stringFlavor )){ 
            return node.toString() ;
        } else {
            throw new UnsupportedFlavorException(flavor) ;
        }
    }

}
