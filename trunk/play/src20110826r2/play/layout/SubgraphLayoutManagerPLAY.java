package play.layout;

import higraph.view.NodeView;
import higraph.view.layout.SgLayoutManager;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.tags.PLAYTags;
import play.view.NodeViewPLAY;
import play.view.SubGraphViewPLAY;

/**
 * @author  Charles
 */
public class SubgraphLayoutManagerPLAY
		extends
		SgLayoutManager<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	/**
	 * @uml.property  name="mySGV"
	 * @uml.associationEnd  
	 */
	protected SubGraphViewPLAY mySGV;
	/**
	 * @uml.property  name="lmf"
	 * @uml.associationEnd  
	 */
	private LayoutManagerFactoryPLAY lmf = new LayoutManagerFactoryPLAY();

	public SubgraphLayoutManagerPLAY(SubGraphViewPLAY view) {
		super(view);
		this.mySGV = view;
	}

	@Override
	public void layoutLocal() {
		SgLayoutManager<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> lm;
		NodeViewPLAY nodeView;
		PLAYTags tag;

		Iterator<NodeView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> iterator = mySGV
				.getTops();
		while (iterator.hasNext()) {
			nodeView = (NodeViewPLAY) iterator.next();
			tag = nodeView.getNode().getTag();
			lm = lmf.makeLayoutManager(tag, mySGV, nodeView, 0, 0);
			nodeView.setLayoutManager(lm);
			lm.layoutLocal();
		}

		iterator = mySGV.getTops();
		double localY = 0.;
		myExtent.setRect(0., 0., 0., 0.);
		while (iterator.hasNext()) {
			nodeView = (NodeViewPLAY) iterator.next();
			lm = nodeView.getLayoutManager();
			lm.accumulateTranslation(0, localY);
			lm.applyTranslation();
			Rectangle2D.union(myExtent, lm.getExtent(), myExtent);
			localY = myExtent.getMaxY() + 20;
		}

	}
}
