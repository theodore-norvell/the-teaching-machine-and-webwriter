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
import java.util.List;

import play.higraph.model.PLAYTag;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYViewTransferObject implements Transferable {

	private PLAYTag tag;

	private List<PLAYNodeView> viewList;

	public static final DataFlavor TAG_DATAFLAVOR = new DataFlavor(
			PLAYTag.class, "PLAYTag.DataFlavor");

	public static final DataFlavor VIEWLIST_DATAFLAVOR = new DataFlavor(
			List.class, "ViewList.DataFlavor");

	public PLAYViewTransferObject(PLAYTag tag) {
		this.tag = tag;
	}
	
	public PLAYViewTransferObject( List<PLAYNodeView> list) {
		this.viewList =  list;
	}

	/**
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.stringFlavor,
				PLAYViewTransferObject.TAG_DATAFLAVOR,
				PLAYViewTransferObject.VIEWLIST_DATAFLAVOR };
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
		} else if (flavor.equals(PLAYViewTransferObject.VIEWLIST_DATAFLAVOR)) {
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
		} else if (flavor.equals(PLAYViewTransferObject.VIEWLIST_DATAFLAVOR)) {
			return this.viewList;
		}
		return null;
	}

}
