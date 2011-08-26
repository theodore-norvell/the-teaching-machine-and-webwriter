/**
 * RegexNodeToolBarTransferHandler.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * This transfer handler installs on the {@link RegexJList}
 * @author Xiaoyu Guo
 */
public class RegexNodeToolBarTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 6051659623878408438L;

    /**
     * 
     */
    public RegexNodeToolBarTransferHandler() {
    }

    /**
     * @param property
     */
    public RegexNodeToolBarTransferHandler(String property) {
        super(property);
    }

    @Override
    public int getSourceActions(JComponent comp) {
        return COPY;
    }
    
    protected Transferable createTransferable(JComponent list){
        if(list instanceof RegexJList){
            return new RegexNodeTransferObject(((RegexJList)list).createSelectedNode());
        } else {
            return null;
        }
    }
    
    @Override
    protected void exportDone(JComponent source, Transferable data, int action){
        // de-select the source button
        if(source instanceof RegexJList){
            ((RegexJList)source).clearSelection();
        }
    }
}
