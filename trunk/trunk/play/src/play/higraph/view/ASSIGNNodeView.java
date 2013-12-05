/**
 * ASSIGNNodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-05 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import play.executor.Environment;
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
public class ASSIGNNodeView extends PLAYNodeView {
	
	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    protected ASSIGNNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setNodeSize(150, 50);
	super.setColor(Color.BLACK);
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
	screen.drawString("\uFF1D", (float) (x + 5), (float) (y + 10));

	if (number == 2) {
	    Rectangle2D leftExpNodeViewRect = ((PLAYNodeView) this.getChild(0))
		    .getNextExtent();
	    Rectangle2D rightExpNodeViewRect = ((PLAYNodeView) this.getChild(1))
		    .getNextExtent();

	    // draw an image of assign
	    screen.drawString(
		    "\u2254",
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
		System.out.println("inside assign execute");
		int mChildren = n.getNumberOfChildren();
		System.out.println("children = "+mChildren);
		
		PLAYNode child1 = n.getChild(0);
		PLAYNode child2 = n.getChild(1);
		System.out.println("child1 = "+child1.getTag());
		//String var = child1.getView().execute(e, child1, sg);
		highlight(child1);
		String var = child1.getPayload().getPayloadValue();
		System.out.println("child2 = "+child2.getTag());
		String value = child2.getView().execute(e, child2, sg);
			
		if(!e.has(var)){
			e.add(var);
			e.set(var, value);
		}
		else{
			e.set(var, value);
		}
		System.out.println(var + " = "+ value);
		new PLAYEdge(child1,child2,new PLAYEdgeLabel(var),sg.getWholeGraph());
		return s;		
	}

}
