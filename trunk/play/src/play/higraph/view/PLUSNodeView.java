/**
 * PLUSNodeView.java - play.higraph.view - PLAY
 * Created on 2012-07-26 by Kai Zhu
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
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai
 * 
 */
public class PLUSNodeView extends PLAYNodeView {

	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;
	private PLAYHigraphJComponent hj;

    public PLUSNodeView(
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
	// draw a plus mark
	screen.drawString("+", (float) (x + 5), (float) (y + 10));

	if (number == 2) {
	    Rectangle2D leftExpNodeViewRect = ((PLAYNodeView) this.getChild(0))
		    .getNextExtent();
	    Rectangle2D rightExpNodeViewRect = ((PLAYNodeView) this.getChild(1))
		    .getNextExtent();

	    // draw an image of plus
	    screen.drawString(
		    "\u00B1",
		    (int) (leftExpNodeViewRect.getMaxX() + (rightExpNodeViewRect
			    .getMinX() - leftExpNodeViewRect.getMaxX()) / 3),
		    (int) (rightExpNodeViewRect.getMinY() + rightExpNodeViewRect
			    .getHeight() / 2));
		}
    }
    
    //added by ravneet
    public String execute(Environment env,PLAYNode node,PLAYSubgraph sgraph){
		e = env;
		s = null;
		sg = sgraph;
		n = node;		
		
		highlight(n);
		
		int mChildren = n.getNumberOfChildren();
		System.out.println("children = "+mChildren);
		
		for(int k=0;k<mChildren;k++){
			System.out.println(n.getChild(k).getTag());
			
		}
		
		if(mChildren ==2){
			
			PLAYNode child1 = n.getChild(0);
			PLAYNode child2 = n.getChild(1);
			String value1 = child1.getView().execute(e, child1, sg);
			String value2 = child2.getView().execute(e, child2, sg);
			System.out.println("x = "+value1+" y = "+value2 );
			
			if((isNumber(value1))&&(isNumber(value2))){
				s = Integer.toString((Integer.parseInt(value1)+Integer.parseInt(value2)));
			}
		}
		
		return s;
		
	}

}
