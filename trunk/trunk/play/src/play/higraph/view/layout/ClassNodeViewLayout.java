/**
 * IFNodeViewLayout.java - play.higraph.view.layout - PLAY
 * 
 * Created on 2012-03-08 by Kai Zhu
 */
package play.higraph.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.view.ClassNodeView;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class ClassNodeViewLayout extends PLAYLayoutManager {

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
		if (nodeView instanceof ClassNodeView) {
			ClassNodeView classNodeView = (ClassNodeView) nodeView;
			Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 200, 200);
			int num = classNodeView.getNumChildren();
			System.out.println("num = "+num);

			if (num >= 2) {
				double x = 30;
				double y = 0;
				for (int i = 0; i < num; i++) {
					PLAYNodeView childNodeView = (PLAYNodeView) classNodeView
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
				classNodeView.setNextShape(rectangle);
				classNodeView.translateNextHierarchy(0, 0);
			} else {
				classNodeView.placeNext(0, 0);
			}
		}

	}
}
