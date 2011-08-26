package play.view;

import higraph.view.BranchView;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class BranchViewPLAY
		extends
		BranchView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	protected BranchViewPLAY(ViewFactoryPLAY vf, SubGraphViewPLAY view,
			NodeViewPLAY nv, Color c, Stroke s, GeneralPath p) {
		super(vf, view, nv, c, s, p);
		// TODO Auto-generated constructor stub
	}

}
