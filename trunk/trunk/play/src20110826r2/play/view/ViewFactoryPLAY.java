package play.view;

import higraph.view.NodeView;
import higraph.view.SubgraphView;
import higraph.view.ViewFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class ViewFactoryPLAY
		extends
		ViewFactory<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	public ViewFactoryPLAY() {
		this.nodeShape = new Rectangle2D.Double(0, 0, DEFAULT_SHAPE_WIDTH,
				DEFAULT_SHAPE_WIDTH);
		this.nodeFillColor = Color.white;
		this.nodeColor = Color.black;
	}

	@Override
	public SubGraphViewPLAY makeSubgraphView(SubGraphPLAY SG, Component display) {
		return new SubGraphViewPLAY(this, SG, display);
	}

	@Override
	public NodeViewPLAY makeNodeView(
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> sgv,
			NodePLAY node, Color c, Color f, Stroke s, RectangularShape r) {
		NodeViewPLAY nv = new NodeViewPLAY(this, sgv, node, f, f, s, r);
		return nv;
	}

	@Override
	public NodeViewPLAY makeNodeView(
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> sgv,
			NodePLAY node) {
		NodeViewPLAY nv = makeNodeView(sgv, node, nodeColor, nodeFillColor,
				nodeStroke, nodeShape);
		return nv;
	}

	@Override
	public EdgeViewPLAY makeEdgeView(
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> sgv,
			EdgePLAY e, Color c, Stroke s, GeneralPath p) {
		return new EdgeViewPLAY(this, sgv, e, c, s, p);

	}

	@Override
	public EdgeViewPLAY makeEdgeView(
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> sgv,
			EdgePLAY e) {
		return makeEdgeView(sgv, e, edgeColor, edgeStroke, new GeneralPath());
	}

	@Override
	public DropZonePLAY makeDropZone(
			NodeView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> nv,
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> sgv,
			Color c, Color f, Stroke s, RectangularShape rs) {
		// TODO Auto-generated method stub
		return new DropZonePLAY(this, nv, sgv, f, f, s, rs);
	}

	public void setNodeColor(int c) {
		nodeColor = new Color(c);
	}

	public Color getNodeColor() {
		return nodeColor;
	}

	public void setNodeFillColor(int c) {
		nodeFillColor = new Color(c);
	}

	public Color getNodeFillColor() {
		return nodeFillColor;
	}

	public void setNodeStroke(Stroke s) {
		nodeStroke = s;
	}

	public Stroke getNodeStroke() {
		return nodeStroke;
	}

	public RectangularShape getNodeShape() {
		return nodeShape;
	}

	public void setEdgeColor(int c) {
		edgeColor = new Color(c);
	}

	public Color getEdgeColor() {
		return edgeColor;
	}

	public void setEdgeStroke(Stroke s) {
		edgeStroke = s;
	}

	public Stroke getEdgeStroke() {
		return edgeStroke;
	}

	public void setBranchColor(int c) {
		branchColor = new Color(c);
	}

	public Color getBranchColor() {
		return branchColor;
	}

	public Stroke getBranchStroke() {
		return branchStroke;
	}
}
