/**
 * IFNodeViewLayout.java - play.higraph.view.layout - PLAY
 * 
 * Ravneet
 */
package play.higraph.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.view.MethodNodeView;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Ravneet
 * 
 */
public class MethodNodeViewLayout extends PLAYLayoutManager {

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
		if (nodeView instanceof MethodNodeView) {
			MethodNodeView methodNodeView = (MethodNodeView) nodeView;
			Rectangle2D rectangle = new Rectangle2D.Double(10, 10, 240, 100);
			int num = methodNodeView.getNumChildren();
			//System.out.println("num = "+num);

			if (num >= 3) {
				//System.out.println("inside num check");
				double x = 30;
				double y = 30;
				for (int i = 0; i < num; i++) {
					PLAYNodeView childNodeView = (PLAYNodeView) methodNodeView
							.getChild(i);
					childNodeView.doLayout();
					childNodeView.placeNextHierarchy(x, y);
					Rectangle2D childNextExtent = childNodeView.getNextExtent();
					y += 10 + childNextExtent.getHeight();
					Rectangle2D.union(rectangle, childNextExtent, rectangle);
					childNodeView.getBranch().setVisibility(false);
				}
				rectangle.add(new Point2D.Double(rectangle.getMaxX() + 10,
						rectangle.getMaxY() + 10));
				methodNodeView.setNextShape(rectangle);
				methodNodeView.translateNextHierarchy(0, 0);
			} else {
				methodNodeView.placeNext(0, 0);
			}
		}

	}
}
