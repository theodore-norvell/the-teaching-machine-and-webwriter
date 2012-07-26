/**
 * SIGNNodeViewLayout.java - play.higraph.view.layout - PLAY
 * Created on 2012-07-26 by Kai Zhu
 */
package play.higraph.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.view.DIVISIONNodeView;
import play.higraph.view.MINUSNodeView;
import play.higraph.view.MULTIPLICATIONNodeView;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLUSNodeView;

/**
 * @author Kai
 * 
 */
public class SIGNNodeViewLayout extends PLAYLayoutManager {

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
	if ((nodeView instanceof PLUSNodeView)
		|| (nodeView instanceof MINUSNodeView)
		|| (nodeView instanceof MULTIPLICATIONNodeView)
		|| (nodeView instanceof DIVISIONNodeView)) {
	    PLAYNodeView playNodeView = (PLAYNodeView) nodeView;
	    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 20, 20);
	    if (playNodeView.getNumChildren() == 2) {
		double x = 5;
		double y = 15;
		for (int i = 0; i < 2; i++) {
		    PLAYNodeView childNodeView = (PLAYNodeView) playNodeView
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
		playNodeView.setNextShape(rectangle);
		playNodeView.translateNextHierarchy(0, 0);
	    } else {
		playNodeView.placeNext(0, 0);
	    }
	}
    }

}
