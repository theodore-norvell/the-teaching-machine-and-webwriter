/**
 * WHILENodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-12 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.Graphics2D;
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
public class WHILENodeView extends PLAYNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public WHILENodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setColor(Color.DARK_GRAY);
	super.setFillColor(null);
    }

    /**
     * @see higraph.view.NodeView#drawSelf(java.awt.Graphics2D)
     */
    @Override
    protected void drawSelf(Graphics2D screen) {
	double x = super.getNextX();
	double y = super.getNextY();
	double width = super.getNextWidth();
	// double height = super.getNextHeight();
	int number = this.getNumChildren();

	super.drawSelf(screen);

	// draw a Circle mark
	screen.drawString("\u25EF", (float) (x + 5), (float) (y + 10));

	if (number == 3) {
	    Rectangle2D expPlaceHolderNodeVieRect = ((PLAYNodeView) this
		    .getChild(0)).getNextExtent();
	    Rectangle2D doSeqNodeViewRect = ((PLAYNodeView) this.getChild(1))
		    .getNextExtent();
	    Rectangle2D elseSeqNodeViewRect = ((PLAYNodeView) this.getChild(2))
		    .getNextExtent();

	    // draw a line between place hold area and do branch
	    screen.drawLine(
		    (int) x,
		    (int) (expPlaceHolderNodeVieRect.getMaxY() + (doSeqNodeViewRect
			    .getMinY() - expPlaceHolderNodeVieRect.getMaxY()) / 2),
		    (int) (x + width),
		    (int) (expPlaceHolderNodeVieRect.getMaxY() + (doSeqNodeViewRect
			    .getMinY() - expPlaceHolderNodeVieRect.getMaxY()) / 2));

	    // draw an image of do branch
	    screen.drawString("\u21BA", (int) (x + 10),
		    (int) (doSeqNodeViewRect.getMinY() + 20));
	    // draw a line between then branch and else branch
	    screen.drawLine((int) x,
		    (int) (doSeqNodeViewRect.getMaxY() + (elseSeqNodeViewRect
			    .getMinY() - doSeqNodeViewRect.getMaxY()) / 2),
		    (int) (x + width),
		    (int) (doSeqNodeViewRect.getMaxY() + (elseSeqNodeViewRect
			    .getMinY() - doSeqNodeViewRect.getMaxY()) / 2));
	    // draw an image of else branch
	    screen.drawString("\u21B7", (int) (x + 10),
		    (int) (elseSeqNodeViewRect.getMinY() + 20));
	}
    }

}
