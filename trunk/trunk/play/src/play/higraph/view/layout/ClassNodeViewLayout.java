/**
 *CLASSNodeViewLayout.java - play.higraph.view.layout - PLAY
 * 
 */
package play.higraph.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import play.higraph.view.ClassNodeView;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLAYViewFactory;

/**
 * @author Ravneet
 * 
 */
public class ClassNodeViewLayout extends PLAYLayoutManager {

	/**
	 * @see play.higraph.view.layout.PLAYLayoutManager#layoutLocal(play.higraph.view.PLAYHigraphView)
	 */
	@Override
	public void layoutLocal(PLAYHigraphView higraphView) {
		//System.out.println("In layoutLocal for higraphView of ClassNodeView") ;
	}

	/**
	 * @see play.higraph.view.layout.PLAYLayoutManager#layoutLocal(play.higraph.view.PLAYNodeView)
	 */
	@Override
	public void layoutLocal(PLAYNodeView nodeView) {
		//System.out.println("In layoutLocal of ClassNodeView") ;
		if (nodeView instanceof ClassNodeView) {
			ClassNodeView classNodeView = (ClassNodeView) nodeView;
			classNodeView.removeAllDropZones();
			PLAYViewFactory viewFactory = (PLAYViewFactory) classNodeView
					.getHigraphView().getViewFactory();
			Rectangle2D rectangle = new Rectangle2D.Double(20, 20, 250, 280);
			int num = classNodeView.getNumChildren();
			
			//System.out.println("num = "+num);

			if (num >= 1) {
				//System.out.println("inside num check");
				double x = 30;
				double y = 40;
				for (int i = 0; i < num; i++) {
					PLAYNodeView childNodeView = (PLAYNodeView) classNodeView
							.getChild(i);
					childNodeView.doLayout();
					childNodeView.placeNextHierarchy(x, y);
					Rectangle2D childNextExtent = childNodeView.getNextExtent();
					y += 20 + childNextExtent.getHeight();
					Rectangle2D.union(rectangle, childNextExtent, rectangle);
					childNodeView.getBranch().setVisibility(false);
					if(!(i==num-1)){
						viewFactory.makeDropZone(classNodeView);
					}
				}
				rectangle.add(new Point2D.Double(rectangle.getMaxX() + 10,
						rectangle.getMaxY() + 20));
				classNodeView.setNextShape(rectangle);
				classNodeView.translateNextHierarchy(0, 0);
			} else {
				classNodeView.placeNext(0, 0);
			}
		}

	}
}
