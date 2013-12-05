/**
 * GreaterNodeView.java - play.higraph.view - PLAY
 * Created on 2013-10-19 by Ravneet Sandhu
 */
package play.higraph.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import higraph.view.HigraphView;
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
 * @author Kai
 * 
 */
public class LessThanNodeView extends PLAYNodeView {

	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;
	
    public LessThanNodeView(
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
	//screen.drawString("\uFF0D", (float) (x + 5), (float) (y + 10));

	if (number == 2) {
	    Rectangle2D leftExpNodeViewRect = ((PLAYNodeView) this.getChild(0))
		    .getNextExtent();
	    Rectangle2D rightExpNodeViewRect = ((PLAYNodeView) this.getChild(1))
		    .getNextExtent();

	    // draw an image of assign
	    screen.drawString(
		    "\u003C",
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
		
		System.out.println("inside less than execute");
		int children = n.getNumberOfChildren();
		System.out.println("children = "+children);
		
		if(children ==2){
			
			PLAYNode child1 = n.getChild(0);
			PLAYNode child2 = n.getChild(1);
			String value1 = child1.getView().execute(e, child1, sg);
			String value2 = child2.getView().execute(e, child2, sg);
			System.out.println(value1 + "  -  "+value2);
			if(value1 != null && value2 != null){
				if((Integer.parseInt(value1))<(Integer.parseInt(value2))){
					s = "true";
				}
				else{
					s = "false";
				}
			}
					}
				
		return s;
	}
}
