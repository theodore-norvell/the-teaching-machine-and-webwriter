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

import play.executor.Environment;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class MethodNodeView extends PLAYNodeView {
	
	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;
	private PLAYHigraphJComponent hj;

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	protected MethodNodeView(
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
		//int number = this.getNumChildren();
		
		super.drawSelf(screen);
	}
	
	public String execute(Environment env,PLAYNode node,PLAYSubgraph sgraph){
		e = env;
		s = null;
		sg = sgraph;
		n = node;
		
		highlight(n);
		PLAYNode m = n;
		//m.setView(n.getView());
		
		int mChildren = m.getNumberOfChildren();
		System.out.println("children = "+mChildren);
		
		for(int k=0;k<mChildren;k++){
			System.out.println(m.getChild(k).getTag());
			
			
				s= m.getChild(k).getView().execute(env, m.getChild(k), sg);
		}
		
		return s;
		
	}

}
