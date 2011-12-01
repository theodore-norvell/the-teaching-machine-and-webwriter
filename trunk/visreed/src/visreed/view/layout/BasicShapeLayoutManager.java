/**
 * BasicShapeLayoutManager.java
 * 
 * @date: Nov 30, 2011
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
public class BasicShapeLayoutManager extends VisreedNodeLayoutManager {
	/* (non-Javadoc)
	 * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView)
	 */
	@Override
	public void layoutNode(VisreedNodeView nv) {
		if(nv == null){
			return;
		}
		
		// decode parameters
		Offset padding = nv.getLayoutParameter().getPadding();
		
		// do layout
		nv.placeNext(0, 0);
		Rectangle2D newExtent = new Rectangle2D.Double(
			0,
			0,
			padding.getLeft() + padding.getRight(),
			padding.getTop() + padding.getBottom()
		);
		
		if(!newExtent.equals(nv.getNextShapeExtent())){
			nv.setNextShape(newExtent);
		}
	}

	private static final VisreedNodeLayoutManager instance = new BasicShapeLayoutManager();
	public static VisreedNodeLayoutManager getInstance(){
		return instance;
	}

}
