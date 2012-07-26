/**
 * MINUSNodeView.java - play.higraph.view - PLAY
 * Created on 2012-07-26 by Kai Zhu
 */
package play.higraph.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import higraph.view.HigraphView;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai
 * 
 */
public class MINUSNodeView extends PLAYNodeView {

    public MINUSNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setNodeSize(50, 50);
	super.setColor(Color.RED);
	super.setFillColor(null);
    }

    /**
     * @see higraph.view.NodeView#drawSelf(java.awt.Graphics2D)
     */
    @Override
    protected void drawSelf(Graphics2D screen) {
	double x = super.getNextX();
	double y = super.getNextY();
	// double width = super.getNextWidth();
	// double height = super.getNextHeight();
	int number = this.getNumChildren();

	super.drawSelf(screen);
	// draw a assign mark
	screen.drawString("\uFF0D", (float) (x + 5), (float) (y + 10));

	if (number == 2) {
	    Rectangle2D leftExpNodeViewRect = ((PLAYNodeView) this.getChild(0))
		    .getNextExtent();
	    Rectangle2D rightExpNodeViewRect = ((PLAYNodeView) this.getChild(1))
		    .getNextExtent();

	    // draw an image of assign
	    screen.drawString(
		    "\uFF0D",
		    (int) (leftExpNodeViewRect.getMaxX() + (rightExpNodeViewRect
			    .getMinX() - leftExpNodeViewRect.getMaxX()) / 3),
		    (int) (rightExpNodeViewRect.getMinY() + rightExpNodeViewRect
			    .getHeight() / 2));
	}
    }

}
