/**
 * SytanxTransferHandler.java - play.higraph.swing - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.swing;

import java.awt.datatransfer.Transferable;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;

import play.higraph.model.PLAYTag;
import play.higraph.view.PLAYNodeView;
import tm.utilities.Assert;

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
		PLAYTag playTag = null;
		if (c instanceof JList) {
			JList<?> list = (JList<?>) c;
			if (!list.isSelectionEmpty()) {
				playTag = (PLAYTag) list.getSelectedValue();
			} else {
				return null;
			}
		} else if (c instanceof JTree) {
			JTree tree = (JTree) c;
			if (tree.getSelectionCount() == 1) {
				playTag = (PLAYTag) ((DefaultMutableTreeNode) tree.getSelectionPath()
						.getLastPathComponent()).getUserObject();
			}
		} else {
			return null;
		}
		Assert.check(playTag != null ) ;
		return new PLAYViewTransferObject(playTag);
	}

}
