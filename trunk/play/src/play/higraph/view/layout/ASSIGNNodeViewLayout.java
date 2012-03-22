/**
 * ASSIGNNodeViewLayout.java - play.higraph.view.layout - PLAY
 * 
 * Created on 2012-03-12 by Kai Zhu
 */
package play.higraph.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.view.ASSIGNNodeView;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class ASSIGNNodeViewLayout extends PLAYLayoutManager {

    /**
     * @see play.higraph.view.layout.PLAYLayoutManager#layoutLocal(play.higraph.view.PLAYHigraphView)
     */
    @Override
    public void layoutLocal(PLAYHigraphView higraphView) {
    }

    /**
     * @see play.higraph.view.layout.PLAYLayoutManager#layoutLocal(play.higraph.view.PLAYNodeView)
     */
    @Override
    public void layoutLocal(PLAYNodeView nodeView) {
	if (nodeView instanceof ASSIGNNodeView) {
	    ASSIGNNodeView assignNodeView = (ASSIGNNodeView) nodeView;
	    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 20, 20);
	    if (assignNodeView.getNumChildren() == 2) {
		double x = 5;
		double y = 15;
		for (int i = 0; i < 2; i++) {
		    PLAYNodeView childNodeView = (PLAYNodeView) assignNodeView
			    .getChild(i);
		    childNodeView.doLayout();
		    childNodeView.placeNextHierarchy(x, y);
		    Rectangle2D childNextExtent = childNodeView.getNextExtent();
		    x += 20 + childNextExtent.getWidth();
		    Rectangle2D.union(rectangle, childNextExtent, rectangle);
		    childNodeView.getBranch().setVisibility(false);
		}
		rectangle.add(new Point2D.Double(rectangle.getMaxX() + 10,
			rectangle.getMaxY() + 10));
		assignNodeView.setNextShape(rectangle);
		assignNodeView.translateNextHierarchy(0, 0);
	    } else {
		assignNodeView.placeNext(0, 0);
	    }
	}
    }

}
