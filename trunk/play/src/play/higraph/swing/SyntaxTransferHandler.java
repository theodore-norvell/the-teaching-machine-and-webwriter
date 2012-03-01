/**
 * SytanxTransferHandler.java - play.higraph.swing - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.swing;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * @author Kai Zhu
 * 
 */
public class SyntaxTransferHandler extends TransferHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 3714571964205789867L;

    /**
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    @Override
    public int getSourceActions(JComponent c) {
	return TransferHandler.COPY;
    }

    /**
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
	JList<?> list = (JList<?>) c;
	if (!list.isSelectionEmpty()) {
	    Object object = list.getSelectedValue();
	    return new PLAYViewTransferObject(object);
	}
	return null;
    }

}
