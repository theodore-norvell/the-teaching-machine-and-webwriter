/**
 * PLAYViewTransferObject.java - play.higraph.swing - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import play.higraph.model.PLAYTag;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYViewTransferObject implements Transferable {

    private PLAYTag tag;

    public static final DataFlavor THE_TAG_DATAFLAVOR = new DataFlavor(
	    PLAYTag.class, "PLAYTag.DataFlavor");

    public PLAYViewTransferObject(Object object) {
	if (object instanceof PLAYTag) {
	    this.tag = (PLAYTag) object;
	}
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { DataFlavor.stringFlavor,
		PLAYViewTransferObject.THE_TAG_DATAFLAVOR };
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
	if (flavor.equals(DataFlavor.stringFlavor)) {
	    return true;
	} else if (flavor.equals(PLAYViewTransferObject.THE_TAG_DATAFLAVOR)) {
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
	} else if (flavor.equals(PLAYViewTransferObject.THE_TAG_DATAFLAVOR)) {
	    return this.tag;
	}
	return null;
    }

}
