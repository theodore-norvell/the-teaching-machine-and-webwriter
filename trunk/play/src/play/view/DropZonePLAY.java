package play.view;

import higraph.view.DropZone;
import higraph.view.NodeView;
import higraph.view.SubgraphView;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

/**
 * @author  Charles
 */
public class DropZonePLAY
		extends
		DropZone<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	/**
	 * @uml.property  name="index"
	 */
	protected int index;
	/**
	 * @uml.property  name="an"
	 * @uml.associationEnd  
	 */
	protected NodeViewPLAY an;

	protected DropZonePLAY(
			ViewFactoryPLAY vf,
			NodeView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> an,
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> v,
			Color c, Color f, Stroke s, RectangularShape rs) {
		super(vf, an, v, c, f, s, rs);
		this.an = (NodeViewPLAY) an;
		this.index = 0;
	}

	@Override
	public NodeViewPLAY getAssociatedNode() {
		// TODO Auto-generated method stub
		return this.an;
	}

	public void setIndex() {
		this.index = this.an.getZoneList().indexOf(this) + 1;
	}

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}

}
