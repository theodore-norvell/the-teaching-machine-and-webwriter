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
public class ClassNodeView extends PLAYNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	protected ClassNodeView(
			HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
			PLAYNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
		super.setColor(Color.BLUE);
		super.setFillColor(null);
		System.out.println("classview");
	}

	/**
	 * @see higraph.view.NodeView#drawSelf(java.awt.Graphics2D)
	 */
	@Override
	protected void drawSelf(Graphics2D screen) {
		System.out.println("drawself class");
		double x = super.getNextX();
		double y = super.getNextY();
		double width = super.getNextWidth();
		// double height = super.getNextHeight();
		int number = this.getNumChildren();
		System.out.println("children = " + number);

		super.drawSelf(screen);

		// draw a question mark
		//screen.drawString("\uFF1F", (float) (x + 5), (float) (y + 10));

		if (number >= 2) {
			Rectangle2D vardeclRect = ((PLAYNodeView) this
					.getChild(0)).getNextExtent();
			Rectangle2D methodRect = ((PLAYNodeView) this.getChild(1))
					.getNextExtent();

			// draw a line between place hold area and then branch
			screen.drawLine(
					(int) x,
					(int) (vardeclRect.getMaxY() + (methodRect
							.getMinY() - vardeclRect.getMaxY()) / 2),
							(int) (x + width),
							(int) (vardeclRect.getMaxY() + (methodRect
									.getMinY() - vardeclRect.getMaxY()) / 2));

			// draw an image of then branch
			screen.drawString("\u2714", (int) (x + 10),
					(int) (methodRect.getMinY() + 20));
			// draw a line between then branch and else branch



		}
	}

}
