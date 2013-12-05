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
import play.higraph.view.VarDeclNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class VarDeclNodeViewLayout extends PLAYLayoutManager {

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
		if (nodeView instanceof VarDeclNodeView) {
			VarDeclNodeView vNodeView = (VarDeclNodeView) nodeView;
			Rectangle2D rectangle = new Rectangle2D.Double(10, 10, 240, 40);
			int num = vNodeView.getNumChildren();
			//System.out.println("num = "+num);

			if (vNodeView.getNumChildren() == 2) {
				double x = 15;
				double y = 20;
				for (int i = 0; i < 2; i++) {
					PLAYNodeView childNodeView = (PLAYNodeView) vNodeView
							.getChild(i);
					childNodeView.doLayout();
					childNodeView.placeNextHierarchy(x, y);
					Rectangle2D childNextExtent = childNodeView.getNextExtent();
					x += 30 + childNextExtent.getWidth();
					Rectangle2D.union(rectangle, childNextExtent, rectangle);
					childNodeView.getBranch().setVisibility(false);
				}
				rectangle.add(new Point2D.Double(rectangle.getMaxX() + 10,
						rectangle.getMaxY() + 10));
				vNodeView.setNextShape(rectangle);
				vNodeView.translateNextHierarchy(0, 0);
			} else {
				vNodeView.placeNext(0, 0);
			}
		}

	}
}
