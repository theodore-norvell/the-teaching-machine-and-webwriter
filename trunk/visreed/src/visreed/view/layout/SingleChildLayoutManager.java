/**
 * SingleChildLayoutManager.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import java.awt.geom.Rectangle2D;

import visreed.view.VisreedNodeView;

/**
 * This is the unified LayoutManager for all single child nodes.
 * @author Xiaoyu Guo
 */
public class SingleChildLayoutManager extends VisreedNodeLayoutManager {

	/* (non-Javadoc)
	 * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView)
	 */
	@Override
	public void layoutNode(VisreedNodeView nv) {
		if(nv == null || nv.getNumChildren() == 0 || nv.getChild(0) == null){
			return;
		}
		
		// decode parameters
		VisreedNodeView kid = nv.getVisreedChild(0);
		Offset padding = nv.getLayoutParameter().getPadding();
		
		// do layout
		kid.doLayout();
		kid.placeNextHierarchy(padding.getLeft(), padding.getTop());
		Rectangle2D kidExtent = kid.getNextShapeExtent();
		
		nv.placeNext(0, 0);
		Rectangle2D newExtent = new Rectangle2D.Double(
			0,
			0,
			kidExtent.getWidth() + padding.getLeft() + padding.getRight(),
			kidExtent.getHeight() + padding.getTop() + padding.getBottom()
		);
		
		if(!newExtent.equals(nv.getNextShapeExtent())){
			nv.setNextShape(newExtent);
		}
	}

	private static final VisreedNodeLayoutManager instance = new SingleChildLayoutManager();
	public static VisreedNodeLayoutManager getInstance(){
		return instance;
	}
}
