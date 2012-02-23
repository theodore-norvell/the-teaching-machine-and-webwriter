package play.view;

import higraph.view.ZoneView;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class ZoneViewPLAY
		extends
		ZoneView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	protected ZoneViewPLAY(ViewFactoryPLAY vf, SubGraphViewPLAY view,
			Color color, Color fillColor, Stroke stroke,
			RectangularShape theShape) {
		super(vf, view, color, fillColor, stroke, theShape);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getViewType() {
		// TODO Auto-generated method stub
		return null;
	}

}
