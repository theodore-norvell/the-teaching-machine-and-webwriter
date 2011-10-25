/**
 * RegexpSpecLayoutManager.java
 * 
 * @date: Oct 23, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import visreed.model.Direction;
import visreed.view.VisreedNodeView;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexpSpecLayoutManager extends VisreedNodeLayoutManager {
    private static final double TITLE_HEIGHT_PIXEL = 15;
    
    @Override
    public void layoutNode(VisreedNodeView nv, double px, double py) {
    	if(nv == null){return;}

    	VisreedNodeView kid = nv.getVisreedChild(0);
    	kid.doLayout();
    	
    	Rectangle2D extent = kid.getNextShapeExtent();
    	Rectangle2D newExtent = new Rectangle2D.Double(
    		extent.getX(),
    		extent.getY(),
    		extent.getWidth(),
    		extent.getHeight() + TITLE_HEIGHT_PIXEL
		);
    	
    	nv.setNextShape(newExtent);
    	
    	for(int i = 0; i < nv.getNumChildren(); i++){
    		nv.getChild(i).translateNextHierarchy(0, TITLE_HEIGHT_PIXEL);
    	}
    	
    	nv.placeNextHierarchy(px, py);
    	newExtent = nv.getNextShapeExtent();
    	
    	// handle entry & exit points
    	double entryPointY = newExtent.getMaxY() - extent.getHeight() / 2.0;
    	Point2D.Double entryP = null, exitP = null;
    	if(nv.getCurrentDirection().equals(Direction.EAST)){
    		entryP = new Point2D.Double(newExtent.getX(), entryPointY);
    		exitP = new Point2D.Double(newExtent.getMaxX(), entryPointY);
    	} else if (nv.getCurrentDirection().equals(Direction.WEST)){
    		entryP = new Point2D.Double(newExtent.getX(), entryPointY);
    		exitP = new Point2D.Double(newExtent.getMaxX(), entryPointY);
    	}
    	if(entryP != null && exitP != null){
	    	nv.setEntryPoint(entryP);
	    	nv.setExitPoint(exitP);
    	}
    }
    
    private static RegexpSpecLayoutManager instance = new RegexpSpecLayoutManager();
    
    /**
     * Gets the globally unique instance of this class. (Singleton Pattern)
     * @return
     */
    public static VisreedNodeLayoutManager getInstance() {
        return instance;
    }
}
