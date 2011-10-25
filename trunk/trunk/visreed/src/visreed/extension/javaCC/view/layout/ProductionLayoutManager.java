/**
 * ProductionLayoutManager.java
 * 
 * @date: Oct 3, 2011
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
 */
public class ProductionLayoutManager extends VisreedNodeLayoutManager {
    private static final double VSPACE_TOP_PIXEL = 15;

	protected ProductionLayoutManager() {
		super();
	}
	
    private static final ProductionLayoutManager instance = new ProductionLayoutManager();
    public static ProductionLayoutManager getInstance(){
        return instance;
    }

	/* (non-Javadoc)
	 * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView, double, double)
	 */
	@Override
	public void layoutNode(VisreedNodeView nv, double px, double py) {
        if(nv == null){
            return;
        }
        int numChildren = nv.getNumChildren();
        Rectangle2D[] childrenExtents = new Rectangle2D[numChildren];
        
        double maxOffsetTopY = 0.0;     /** entryY - nextY */
        double maxBottomOffsetY = 0.0;  /** nextBottomY - entryY */
        
        /** entryY - nextY */
        double[] offsetTopYs = new double[numChildren];
        /** pixels to move the child downwards */
        double[] moveDownDistances = new double[numChildren];
        
        double currentX = 0;
        
        // TODO lock(view)
        for(int i = 0; i < numChildren; i++){
            VisreedNodeView kid = nv.getVisreedChild(i);
            if(nv.getCurrentDirection() .equals(Direction.WEST)){
                kid = nv.getVisreedChild(numChildren - i - 1);
            }
            
            // layout a child at (currentX, 0)
            layoutNodes(kid, px + currentX, py);
            childrenExtents[i] = kid.getNextShapeExtent();
            
            // handling baseline
            double offsetTopY = 0.0, offsetBottomY = 0.0;
            if(kid instanceof VisreedNodeView){
                // possible to use EntryPoint 
                offsetTopY = ((VisreedNodeView)kid).getEntryPoint().getY() - childrenExtents[i].getY();
            } else{
                // default NodeView, EntryPoints located at the center of the extent
                offsetTopY = childrenExtents[i].getCenterY() - childrenExtents[i].getY();
            }
            offsetBottomY = childrenExtents[i].getHeight() - offsetTopY;
            offsetTopYs[i] = offsetTopY;
            if(maxOffsetTopY < offsetTopY){
                maxOffsetTopY = offsetTopY;
            }
            if (maxBottomOffsetY < offsetBottomY){
                maxBottomOffsetY = offsetBottomY;
            }
            
            currentX += childrenExtents[i].getWidth();
        }
        
        // currentX now equals to total width of the seq
        
        for(int i = 0; i < numChildren; i++){
            moveDownDistances[i] = maxOffsetTopY - offsetTopYs[i];
        }
        
        // move all the children to the base line
        for(int i = 0; i < numChildren; i++){
            // currently at y=0, go down by moveDownDistances[i]
            
            nv.getChild(i).translateNextHierarchy(
                0, 
                VSPACE_TOP_PIXEL + moveDownDistances[i]
            );
        }

        nv.placeNext(px, py);
        
        Rectangle2D myNextExtent = null;
        // set the size to adapt the children
        myNextExtent = new Rectangle2D.Double(
            px, 
            py, 
            currentX, 
            maxOffsetTopY + maxBottomOffsetY + VSPACE_TOP_PIXEL
        );
        
        // stretch
        if(nv instanceof VisreedNodeView){
            if(numChildren > 0){
                // modify entry and exit point
                double entryPointY = myNextExtent.getY() + VSPACE_TOP_PIXEL + maxOffsetTopY;
                if(nv.getCurrentDirection().equals(Direction.EAST)){
                    ((VisreedNodeView)nv).setEntryPoint(new Point2D.Double(myNextExtent.getX(), entryPointY));
                    ((VisreedNodeView)nv).setExitPoint(new Point2D.Double(myNextExtent.getMaxX(), entryPointY));
                } else if (nv.getCurrentDirection().equals(Direction.WEST)){
                    ((VisreedNodeView)nv).setExitPoint(new Point2D.Double(myNextExtent.getX(), entryPointY));
                    ((VisreedNodeView)nv).setEntryPoint(new Point2D.Double(myNextExtent.getMaxX(), entryPointY));
                }
            }
        }
        nv.setNextShape(myNextExtent);
        
        // TODO unlock(view)
	}

}
