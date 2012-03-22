/**
 * PLAYNodeView.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;
import higraph.view.Label;
import higraph.view.NodeView;
import higraph.view.ZoneView;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYNodeView
	extends
	NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    protected PLAYLabel label;

    public PLAYNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setNodeShapeType(NodeView.ROUND_RECTANGLE);
	super.setFillColor(null);
	this.label = (PLAYLabel) v.getViewFactory().makeLabel(this, "", 0);
	this.label.setFillColor(Color.YELLOW);
	this.label.setShow(false);
	this.addLabel(this.label);
    }

    /**
     * @return the label
     */
    public PLAYLabel getLabel() {
	return label;
    }

    /**
     * Get the index of the zone to associated node view
     * 
     * @param object
     * @return
     */
    public int indexOfZones(Object object) {
	return zones.toList().indexOf(object);
    }

    /**
     * Get the size of the zones in the node view
     * 
     * @return
     */
    public int zonesSize() {
	return super.zones.size();
    }

    /**
     * Remove all zones from the node view
     */
    public void removeAllDropZones() {
	for (ZoneView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> zoneView : super.zones) {
	    super.removeZone(zoneView);
	}
    }

    @Override
    public void moveLabel(
	    Label<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> label) {
	Rectangle2D cr = getNextShapeExtent();
	Rectangle2D lr = label.getLabelMetrics();
	// Into x and y we compute the position for the centre of the label.
	double x;
	double y;
	double halfW = lr.getWidth() / 2.0;
	double halfH = lr.getHeight() / 2.0;

	switch (label.getPlacement()) {
	case Label.EAST:
	case Label.NORTHEAST:
	case Label.SOUTHEAST:
	    x = cr.getMaxX() - halfW;
	    break;
	case Label.WEST:
	case Label.NORTHWEST:
	case Label.SOUTHWEST:
	    x = cr.getMinX() + halfW;
	    break;
	default:
	    x = cr.getCenterX();
	}
	switch (label.getPlacement()) {
	case Label.NORTH:
	case Label.NORTHEAST:
	case Label.NORTHWEST:
	    y = cr.getMinY() + halfH;
	    break;
	case Label.SOUTH:
	case Label.SOUTHEAST:
	case Label.SOUTHWEST:
	    y = cr.getMaxY() - halfH;
	    break;
	default:
	    y = cr.getCenterY();
	}
	Point2D.Double p = label.getNudge();
	x += p.x;
	y += p.y;
	label.placeNext(x - halfW, y - halfH);
    }

    @Override
    protected PLAYNodeView getThis() {
	return this;
    }

}
