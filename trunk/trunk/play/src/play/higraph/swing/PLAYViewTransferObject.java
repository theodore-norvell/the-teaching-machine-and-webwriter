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
import java.util.List;

import play.higraph.model.PLAYTag;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYViewTransferObject implements Transferable {

    private PLAYTag tag;

    private ComponentView<?, ?, ?, ?, ?, ?, ?> componentView;

    private List<?> viewList;

    public static final DataFlavor TAG_DATAFLAVOR = new DataFlavor(
	    PLAYTag.class, "PLAYTag.DataFlavor");

    public static final DataFlavor COMPONENTVIEW_DATAFLAVOR = new DataFlavor(
	    ComponentView.class, "ComponentView.DataFlavor");

    public static final DataFlavor VIEWLIST_DATAFLAVOR = new DataFlavor(
	    List.class, "LIST.DataFlavor");

    public PLAYViewTransferObject(Object object) {
	if (object instanceof PLAYTag) {
	    this.tag = (PLAYTag) object;
	} else if (object instanceof List) {
	    this.viewList = (List<?>) object;

	} else if (object instanceof ComponentView) {
	    this.componentView = (ComponentView<?, ?, ?, ?, ?, ?, ?>) object;
	}
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { DataFlavor.stringFlavor,
		PLAYViewTransferObject.TAG_DATAFLAVOR,
		PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR,
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
	} else if (flavor
		.equals(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR)) {
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
	} else if (flavor
		.equals(PLAYViewTransferObject.COMPONENTVIEW_DATAFLAVOR)) {
	    return this.componentView;
	} else if (flavor.equals(PLAYViewTransferObject.VIEWLIST_DATAFLAVOR)) {
	    return this.viewList;
	}
	return null;
    }

}
