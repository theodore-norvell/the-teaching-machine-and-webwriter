/**
 * SEQNodeViewLayout.java - play.higraph.view.layout - PLAY
 * 
 * Created on 2012-03-12 by Kai Zhu
 */
package play.higraph.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.SEQNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class SEQNodeViewLayout extends PLAYLayoutManager {

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
	if (nodeView instanceof SEQNodeView) {
	    SEQNodeView seqNodeView = (SEQNodeView) nodeView;
	    // remove all drop zones
	    seqNodeView.removeAllDropZones();
	    int number = seqNodeView.getNumChildren();
	    PLAYViewFactory viewFactory = (PLAYViewFactory) seqNodeView
		    .getHigraphView().getViewFactory();
	    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 20, 20);
	    if (number == 0) {
		// the first drop zone
		viewFactory.makeDropZone(seqNodeView);
	    } else {
		double x = 20;
		double y = 20;
		// drop zone before the child
		viewFactory.makeDropZone(seqNodeView);
		PLAYNodeView childNodeView = (PLAYNodeView) seqNodeView
			.getChild(0);
		for (int i = 0; i < number; i++) {
		    // child
		    childNodeView = (PLAYNodeView) seqNodeView.getChild(i);
		    childNodeView.doLayout();
		    childNodeView.placeNextHierarchy(x, y);
		    Rectangle2D childNextExtent = childNodeView.getNextExtent();
		    y += 20 + childNextExtent.getHeight();
		    Rectangle2D.union(rectangle, childNextExtent, rectangle);
		    childNodeView.getBranch().setVisibility(false);
		    // drop zone behind the child
		    viewFactory.makeDropZone(seqNodeView);
		}
	    }
	    rectangle.add(new Point2D.Double(rectangle.getMaxX() + 10,
		    rectangle.getMaxY() + 10));
	    seqNodeView.setNextShape(rectangle);
	    seqNodeView.translateNextHierarchy(0, 0);
	}
    }

}
