/**
 * PLAYBoxesInBoxesLayout.java - play.higraph.view.layout - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.view.layout;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.layout.NestedTreeLayoutManager;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYBoxesInBoxesLayout
	extends
	NestedTreeLayoutManager<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    public PLAYBoxesInBoxesLayout(NestedTreeLayoutManager.Axis axis) {
	super(axis);
    }

    public PLAYBoxesInBoxesLayout getThis() {
	return this;
    }

    /**
     * @see higraph.view.layout.NestedTreeLayoutManager#layoutLocal(higraph.view.HigraphView)
     */
    @Override
    public void layoutLocal(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> hgView) {
	Iterator<NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> iterator = hgView
		.getTops();
	double p = 0.0;
	while (iterator.hasNext()) {
	    NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> top = iterator
		    .next();
	    top.doLayout();
	    Rectangle2D r = top.getNextExtent();
	    double w = r.getWidth();
	    double h = r.getHeight();
	    top.translateNextHierarchy(p * xCoef, p * yCoef);
	    p += w * xCoef + h * yCoef + 10;
	    doDislocations(top);
	}
    }

    /**
     * @see higraph.view.layout.NestedTreeLayoutManager#layoutLocal(higraph.view.NodeView)
     */
    @Override
    public void layoutLocal(
	    NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> nv) {
	final double OFFSET_X = 5;
	final double OFFSET_Y = 5;

	int kids = nv.getNumChildren();

	Rectangle2D r = new Rectangle2D.Double(0, 0, 20, 20);

	if (kids > 0) {
	    double localX = 0;
	    double localY = 0;

	    // put kids inside stepping over from left
	    for (int i = 0; i < kids; i++) {
		localX += OFFSET_X * xCoef;
		localY += OFFSET_Y * yCoef;
		PLAYNodeView kid = (PLAYNodeView) nv.getChild(i);
		kid.doLayout();
		kid.placeNextHierarchy(localX, localY);
		Rectangle2D kidExtent = kid.getNextExtent();
		localX += xCoef * kidExtent.getWidth();
		localY += yCoef * kidExtent.getHeight();
		Rectangle2D.union(r, kidExtent, r);
		kid.getBranch().setVisibility(false);
	    }
	    // expand myExtent past extent of last child
	    r.add(new Point2D.Double(r.getMaxX() + OFFSET_X, r.getMaxY()
		    + OFFSET_Y));
	    nv.setNextShape(r);
	    nv.setFillColor(null); // Huh? TSN
	    nv.translateNextHierarchy(0, 0);
	} else
	    nv.placeNext(0, 0);
    }

}
