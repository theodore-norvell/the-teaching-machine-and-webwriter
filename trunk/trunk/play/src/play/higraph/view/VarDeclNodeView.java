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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import play.controller.Controller;
import play.executor.Environment;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class VarDeclNodeView extends PLAYNodeView {
	
	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;
	private Graphics2D g;
	private PLAYHigraphJComponent hj;

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
		//super.setColor(new Color(200,34,200));
		super.setColor(Color.WHITE);
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
		
		if (number == 2) {
		    Rectangle2D leftExpNodeViewRect = ((PLAYNodeView) this.getChild(0))
			    .getNextExtent();
		    Rectangle2D rightExpNodeViewRect = ((PLAYNodeView) this.getChild(1))
			    .getNextExtent();

		    // draw an image of assign
		    screen.drawString(
			    "-->",
			    (int) (leftExpNodeViewRect.getMaxX() + (rightExpNodeViewRect
				    .getMinX() - leftExpNodeViewRect.getMaxX()) / 3),
			    (int) (rightExpNodeViewRect.getMinY() + rightExpNodeViewRect
				    .getHeight() / 2));
		}
	}
	
	public String execute(Environment env,PLAYNode node,PLAYSubgraph sgraph){
		e = env;
		s = null;
		sg = sgraph;
		n = node;
		
		highlight(n);
		PLAYNode child1 = n.getChild(0);
		PLAYNode child2 = n.getChild(1);
	
		String var = child1.getView().execute(e, child1, sg);
		String value = child2.getView().execute(e, child2, sg);
		if(!e.has(var)){
			e.add(var);
			e.set(var, value);
		}
		System.out.println(var + " = "+ value);
		new PLAYEdge(child1,child2,new PLAYEdgeLabel(var),sg.getWholeGraph());
		
		
		return s;
	}
	
}
