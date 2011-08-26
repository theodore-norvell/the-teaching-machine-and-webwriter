package play.layout;

import higraph.view.layout.SgLayoutManager;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.tags.PLAYTags;
import play.view.DropZonePLAY;
import play.view.NodeViewPLAY;
import play.view.SubGraphViewPLAY;
import play.view.ViewFactoryPLAY;

/**
 * @author  Charles
 */
public class TagLayoutManager
		extends
		SgLayoutManager<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	private final double GAP = 30;
	/**
	 * @uml.property  name="myNodeViewPLAY"
	 * @uml.associationEnd  
	 */
	protected NodeViewPLAY myNodeViewPLAY;
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
	private double px;
	private double py;

	public TagLayoutManager(SubGraphViewPLAY view, NodeViewPLAY nodeView,
			double px, double py) {
		super(view, nodeView);
		this.myNodeViewPLAY = nodeView;
		this.mySGV = view;
		this.px = px;
		this.py = py;
	}

	@Override
	public void applyTranslation() {
		translateNodes(myNodeViewPLAY, dxLayout, dyLayout);
		super.applyTranslation();
	}

	@Override
	public void layoutLocal() {
		layoutNode(myNodeViewPLAY, px, py);

	}

	private void layoutNode(NodeViewPLAY nv, double px, double py) {
		nv.getZoneList().clear();
		double insideX = px + GAP + 15;
		double insideY = py + 15;
		double localMaxX = 0;
		double localMaxY = 0;
		nv.setNextShape(new Rectangle2D.Double(px, py, 60, 30));
		NodeViewPLAY child;
		SgLayoutManager<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> lm;

		if (nv.getNumChildren() > 0) {
			double dzX, dzY, dzW, dzH;

			for (int i = 0; i < nv.getNumChildren(); i++) {

				child = nv.getChild(i);
				lm = lmf.makeLayoutManager(child.getNode().getTag(), mySGV,
						child, insideX, insideY);
				child.setLayoutManager(lm);
				myExtent.setRect(0., 0., 0., 0.);
				lm.layoutLocal();

				if (lm.getExtent().getMaxX() > localMaxX)
					localMaxX = lm.getExtent().getMaxX();
				if (lm.getExtent().getMaxY() > localMaxY)
					localMaxY = lm.getExtent().getMaxY();

				Rectangle2D.union(myExtent, new Rectangle2D.Double(px, py,
						localMaxX - px, localMaxY - py), myExtent);

				DropZonePLAY dz;
				if (nv.getNode().getTag() == PLAYTags.IF
						|| nv.getNode().getTag() == PLAYTags.SEQ
						|| nv.getNode().getTag() == PLAYTags.WHILE
						|| nv.getNode().getTag() == PLAYTags.CLASS) {

					dzY = child.getLayoutManager().getExtent().getMaxY();
					dzX = child.getNextX();
					dzW = localMaxX - dzX;
					dzH = 15;

					dz = nv.getViewFactory().makeDropZone(nv, mySGV,
							Color.gray, Color.gray, null,
							new Rectangle2D.Double(dzX, dzY, dzW, dzH));

					insideY = dzY + dzH;
					if (child.getLayoutManager().getExtent().getMaxY() + 15 > localMaxY)
						localMaxY = child.getLayoutManager().getExtent()
								.getMaxY() + 15;
				} else {
					dzY = child.getNextY();
					dzX = child.getLayoutManager().getExtent().getMaxX();
					dzH = localMaxY - dzY;
					dzW = 15;

					dz = nv.getViewFactory().makeDropZone(nv, mySGV,
							Color.gray, Color.gray, null,
							new Rectangle2D.Double(dzX, dzY, dzW, dzH));
					insideX = dzX + dzW;
					if (child.getLayoutManager().getExtent().getMaxX() + 15 > localMaxX)
						localMaxX = child.getLayoutManager().getExtent()
								.getMaxX() + 15;
				}
				nv.addZone(dz);
				dz.setIndex();
			}
			Rectangle2D.union(myExtent, new Rectangle2D.Double(px, py,
					localMaxX - px, localMaxY - py), myExtent);

			DropZonePLAY firstDZ;
			if (nv.getNode().getTag() == PLAYTags.IF
					|| nv.getNode().getTag() == PLAYTags.WHILE
					|| nv.getNode().getTag() == PLAYTags.SEQ
					|| nv.getNode().getTag() == PLAYTags.CLASS) {
				dzY = nv.getChild(0).getLayoutManager().getExtent().getMaxY();
				dzX = nv.getChild(0).getNextX();
				dzW = localMaxX - insideX;
				dzH = 15;

				firstDZ = nv.getViewFactory().makeDropZone(
						nv,
						mySGV,
						Color.GRAY,
						Color.GRAY,
						null,
						new Rectangle2D.Double(px + 45 + 1, py + 1, dzW - 1,
								dzH - 1));
			} else {
				dzY = nv.getChild(0).getNextY();
				dzX = nv.getChild(0).getLayoutManager().getExtent().getMaxX();
				dzH = localMaxY - insideY;
				dzW = 15;

				firstDZ = ((ViewFactoryPLAY) nv.getViewFactory()).makeDropZone(
						nv, mySGV, Color.GRAY, Color.GRAY, null,
						new Rectangle2D.Double(px + 30 + 1, py + 15 ,
								dzW - 1, dzH ));
			}
			nv.addZone(firstDZ);
		}
		Rectangle2D.union(myExtent, new Rectangle2D.Double(px, py, 60, 30),
				myExtent);
	}

	private void translateNodes(NodeViewPLAY nodeView, double dx, double dy) {
		for (int i = 0; i < nodeView.getNumChildren(); i++)
			translateNodes((NodeViewPLAY) nodeView.getChild(i), dx, dy);
		nodeView.translateNext(dx, dy);
		if (nodeView.getParent() != null)
			nodeView.getBranch().translateNext(dx, dy);
	}

}
