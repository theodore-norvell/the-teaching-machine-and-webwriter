/**
 * IFNodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-04 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
public class VarDeclNodeView extends PLAYNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	protected VarDeclNodeView(
			HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
			PLAYNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
		//super.setNodeSize(200, 200);
		super.setColor(new Color(200,34,200));
		super.setStroke(new BasicStroke(2));
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
		
		screen.setColor(Color.BLUE);
		screen.setFont(new Font("Serif",Font.ITALIC,15));
		screen.drawString("Fields", (float) (x + 5), (float) (y + 12));
	}

}
