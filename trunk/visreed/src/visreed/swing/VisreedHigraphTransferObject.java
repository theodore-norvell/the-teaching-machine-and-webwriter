/**
 * VisreedViewTransferObject.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;

/**
 * VisreedHigraphTransferObject implements {@link Transferable} by given the details 
 * of all the selected nodes in a {@link VisreedWholeGraph}
 * @author Xiaoyu Guo
 */
public class VisreedHigraphTransferObject implements Transferable {
    public static final DataFlavor theNodeListDataFlavor
    = new DataFlavor(List.class, "VisreedNode List DataFlavor") ;

    private VisreedWholeGraph wg;
    
    /**
     * @param view
     */
    public VisreedHigraphTransferObject(VisreedWholeGraph wg) {
        this.wg = wg;
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        // For demonstration, support 3 flavors
        return new DataFlavor[]{ 
            DataFlavor.stringFlavor, 
            VisreedNodeTransferObject.theNodeDataFlavor,
            VisreedHigraphTransferObject.theNodeListDataFlavor
        } ; 
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {

        // For demonstration, support 3 flavors
        if( flavor.equals(DataFlavor.stringFlavor) ) 
            return true ;
        else if (flavor.equals(VisreedNodeTransferObject.theNodeDataFlavor))
            return true;
        else if (flavor.equals(theNodeListDataFlavor))
            return true;
        else return false ;
    }
    
    /* (non-Javadoc)
     * @see higraph.swing.ViewTransferObject#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        
        if( flavor.equals( DataFlavor.stringFlavor )) { 
            return this.wg.toString() ;
        } else if (flavor.equals(VisreedNodeTransferObject.theNodeDataFlavor)){
            if(this.wg != null){
                List<VisreedNode> selected = this.wg.getSelectionNodes();
                if(selected == null || selected.size() == 0){
                    return null;
                } else {
                    return selected.get(0); 
                }
            } else {
                return null;
            }
        }  else if (flavor.equals(theNodeListDataFlavor)){
            if(this.wg != null){
                return this.wg.getSelectionNodes();
            } else {
                return null;
            }
        } else {
            throw new UnsupportedFlavorException(flavor) ;
        }
    }
}
