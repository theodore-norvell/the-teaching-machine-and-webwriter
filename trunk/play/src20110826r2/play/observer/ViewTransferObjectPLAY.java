package play.observer;

import higraph.view.ComponentView;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import play.tags.PLAYTags;
import play.view.NodeViewPLAY;

/**
 * @author  Charles
 */
public class ViewTransferObjectPLAY implements Transferable {

	/**
	 * @uml.property  name="tag"
	 * @uml.associationEnd  
	 */
	private PLAYTags tag;
	/**
	 * @uml.property  name="nv"
	 * @uml.associationEnd  
	 */
	private NodeViewPLAY nv;

	static public final DataFlavor theTagDataFlavor = new DataFlavor(
			PLAYTags.class, "PLAYTags DataFlavor");
	static public final DataFlavor theViewDataFlavor = new DataFlavor(
			ComponentView.class, "ComponentView DataFlavor");

	public ViewTransferObjectPLAY(Object object) {
		if (object instanceof NodeViewPLAY) {
			System.out.println("ob->node");
			this.nv = (NodeViewPLAY) object;
		}else if(object instanceof PLAYTags){
			System.out.println("ob->tag");
			this.tag = (PLAYTags) object;
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(theTagDataFlavor)) {
			return tag;
		} else if (flavor.equals(theViewDataFlavor)) {
			return nv;
		} else
			throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { theTagDataFlavor, theViewDataFlavor,
				DataFlavor.stringFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(DataFlavor.stringFlavor))
			return true;
		else if (flavor.equals(theViewDataFlavor))
			return true;
		else if (flavor.equals(theTagDataFlavor))
			return true;
		else
			return false;
	}

}
