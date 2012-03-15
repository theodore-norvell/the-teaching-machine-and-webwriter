/**
 * IFNodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-04 by Kai Zhu
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
public class IFNodeView extends PLAYNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    protected IFNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setColor(Color.BLUE);
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

	// draw a question mark
	screen.drawString("\uFF1F", (float) (x + 5), (float) (y + 10));

	if (number == 3) {
	    Rectangle2D expPlaceHolderNodeVieRect = ((PLAYNodeView) this
		    .getChild(0)).getNextExtent();
	    Rectangle2D thenSeqNodeViewRect = ((PLAYNodeView) this.getChild(1))
		    .getNextExtent();
	    Rectangle2D elseSeqNodeViewRect = ((PLAYNodeView) this.getChild(2))
		    .getNextExtent();

	    // draw a line between place hold area and then branch
	    screen.drawLine(
		    (int) x,
		    (int) (expPlaceHolderNodeVieRect.getMaxY() + (thenSeqNodeViewRect
			    .getMinY() - expPlaceHolderNodeVieRect.getMaxY()) / 2),
		    (int) (x + width),
		    (int) (expPlaceHolderNodeVieRect.getMaxY() + (thenSeqNodeViewRect
			    .getMinY() - expPlaceHolderNodeVieRect.getMaxY()) / 2));

	    // draw an image of then branch
	    screen.drawString("\u2714", (int) (x + 10),
		    (int) (thenSeqNodeViewRect.getMinY() + 20));
	    // draw a line between then branch and else branch
	    screen.drawLine((int) x,
		    (int) (thenSeqNodeViewRect.getMaxY() + (elseSeqNodeViewRect
			    .getMinY() - thenSeqNodeViewRect.getMaxY()) / 2),
		    (int) (x + width),
		    (int) (thenSeqNodeViewRect.getMaxY() + (elseSeqNodeViewRect
			    .getMinY() - thenSeqNodeViewRect.getMaxY()) / 2));
	    // draw an image of else branch
	    screen.drawString("\u2718", (int) (x + 10),
		    (int) (elseSeqNodeViewRect.getMinY() + 20));
	}
	super.drawSelf(screen);
    }

}
