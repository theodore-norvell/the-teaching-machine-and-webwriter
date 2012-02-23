package play.view;

import higraph.view.EdgeView;
import higraph.view.SubgraphView;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class EdgeViewPLAY
		extends
		EdgeView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	protected EdgeViewPLAY(
			ViewFactoryPLAY vf,
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> v,
			EdgePLAY e, Color c, Stroke s, GeneralPath p) {
		super(vf, v, e, c, s, p);
		// TODO Auto-generated constructor stub
	}

}
