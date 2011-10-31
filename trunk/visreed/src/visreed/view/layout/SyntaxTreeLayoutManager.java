/**
 * SyntaxTreeLayoutManager.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import java.awt.geom.Rectangle2D;

import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class SyntaxTreeLayoutManager extends
    VisreedNodeLayoutManager {    

    /**
     * Recursive routine to layout nodes. Proceeds bottom up, laying
     * out descendants before parents. Updates extent of the
     * layout as it goes. Only the NextShape of the componenttViews
     * are affected.
     * 
     * @param n the nodeview to be layed out
     */
    public void layoutNode(VisreedNodeView n){
		double shapeWidth = n.getNextShapeExtent().getWidth();  // width of the shape of a single node
		double myHeight = n.getNextHeight();
		int kids = n.getNumChildren();
				
		double localX = 0;
		double offset = shapeWidth/2.0;
		// put kids underneath stepping over from left
		for (int i = 0; i < kids; i++){
			VisreedNodeView kid = n.getVisreedChild(i);
			kid.doLayout();
			kid.placeNextHierarchy(localX, 2 * myHeight);
			localX += kid.getNextExtent().getWidth() + offset;
		}
		if (kids > 0) {
			double shapeCenterX = n.getChild(kids/2).getNextShapeExtent().getCenterX(); // center of middle kids shape
			if(kids%2 == 0) { // average of center of two middle kids
				shapeCenterX = (shapeCenterX + n.getChild(kids/2-1).getNextShapeExtent().getCenterX())/2.0; 
			}
			double leftToCenterX = n.getNextShapeExtent().getCenterX()-n.getNextX();
			n.placeNext(shapeCenterX - leftToCenterX, 0);
		} else
			n.placeNext(0, 0);
		Rectangle2D r = n.getNextExtent(); // Correct for any excursion out of the positive quadrant
		double dx = r.getMinX();
		double dy = r.getMinY();
		
		dx = (dx < 0) ? -dx : 0;
		dy = (dy < 0) ? -dy : 0;
				
		// Now apply any correction needed
		if (dx!=0 || dy !=0)
			n.translateNextHierarchy(dx, dy);
	
		for (int i = 0; i < kids; i++){
			layoutBranch(n, n.getChild(i));
		}
		
    }
    
    private static final SyntaxTreeLayoutManager instance = new SyntaxTreeLayoutManager();

    /**
     * @return
     */
    public static VisreedNodeLayoutManager getInstance() {
        return instance;
    }
}
