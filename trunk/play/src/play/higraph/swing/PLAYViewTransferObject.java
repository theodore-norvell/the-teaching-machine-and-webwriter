/**
 * PLAYViewTransferObject.java - play.higraph.swing - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.swing;

import higraph.view.ComponentView;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import play.higraph.model.PLAYTag;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYViewTransferObject implements Transferable {

    private PLAYTag tag;

    private PLAYNodeView nodeView;

    public static final DataFlavor TAG_DATAFLAVOR = new DataFlavor(
	    PLAYTag.class, "PLAYTag.DataFlavor");

    public static final DataFlavor NODEVIEW_DATAFLAVOR = new DataFlavor(
	    PLAYNodeView.class, "PLAYNodeView.DataFlavor");

    public PLAYViewTransferObject(Object object) {
	if (object instanceof PLAYTag) {
	    this.tag = (PLAYTag) object;
	} else if (object instanceof ComponentView) {
	    this.nodeView = (PLAYNodeView) object;
	}
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { DataFlavor.stringFlavor,
		PLAYViewTransferObject.TAG_DATAFLAVOR,
		PLAYViewTransferObject.NODEVIEW_DATAFLAVOR };
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
	if (flavor.equals(DataFlavor.stringFlavor)) {
	    return true;
	} else if (flavor.equals(PLAYViewTransferObject.TAG_DATAFLAVOR)) {
	    return true;
	} else if (flavor.equals(PLAYViewTransferObject.NODEVIEW_DATAFLAVOR)) {
	    return true;
	}
	return false;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public Object getTransferData(DataFlavor flavor)
	    throws UnsupportedFlavorException, IOException {
	if (flavor.equals(DataFlavor.stringFlavor)) {
	    return flavor.toString();
	} else if (flavor.equals(PLAYViewTransferObject.TAG_DATAFLAVOR)) {
	    return this.tag;
	} else if (flavor.equals(PLAYViewTransferObject.NODEVIEW_DATAFLAVOR)) {
	    return this.nodeView;
	}
	return null;
    }

}
