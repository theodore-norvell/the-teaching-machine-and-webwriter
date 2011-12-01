/**
 * RegexNodeToolBarTransferHandler.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.nodebar;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import visreed.swing.VisreedNodeTransferObject;

/**
 * This transfer handler installs on the {@link VisreedNodeToolBar}
 * @author Xiaoyu Guo
 */
public class VisreedNodeToolBarTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 6051659623878408438L;

    /**
     * 
     */
    public VisreedNodeToolBarTransferHandler() {
    }

    /**
     * @param property
     */
    public VisreedNodeToolBarTransferHandler(String property) {
        super(property);
    }

    @Override
    public int getSourceActions(JComponent comp) {
        return COPY;
    }
    
    protected Transferable createTransferable(JComponent list){
        if(list instanceof VisreedNodeToolBar){
            return new VisreedNodeTransferObject(((VisreedNodeToolBar)list).createSelectedNode());
        } else {
            return null;
        }
    }
    
    @Override
    protected void exportDone(JComponent source, Transferable data, int action){
        // de-select the source button
        if(source instanceof VisreedNodeToolBar){
            ((VisreedNodeToolBar)source).clearSelection();
        }
    }
}
