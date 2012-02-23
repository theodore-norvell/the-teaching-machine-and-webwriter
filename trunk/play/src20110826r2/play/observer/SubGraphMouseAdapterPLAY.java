package play.observer;

import higraph.swing.SubgraphMouseAdapter;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.view.SubGraphViewPLAY;

/**
 * @author  Charles
 */
public class SubGraphMouseAdapterPLAY
		extends
		SubgraphMouseAdapter<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	/**
	 * @uml.property  name="observer"
	 * @uml.associationEnd  
	 */
	SubGraphEventObserverPLAY observer;
	/**
	 * @uml.property  name="view"
	 * @uml.associationEnd  
	 */
	SubGraphViewPLAY view;
	TransferHandler th;

	public SubGraphMouseAdapterPLAY(SubGraphViewPLAY view,
			SubGraphEventObserverPLAY observer, TransferHandler th) {
		super(view, observer);
		this.observer = observer;
		this.view = view;
		if (th != null) {
			this.th = th;
		}
	}

	@Override
	public void installIn(JComponent component) {
		if (th instanceof PalletTransferHandler) {
			component.addMouseListener(this);
			component.addMouseMotionListener(this);
			component.setTransferHandler(th);
		} else
			super.installIn(component);
	}

}
