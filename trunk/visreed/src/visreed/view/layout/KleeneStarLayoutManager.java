/**
 * KleeneStarLayoutManager.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import java.awt.geom.Rectangle2D;

import visreed.model.Direction;
import visreed.view.VisreedNodeView;

/**
 * KleeneStarLayoutManager handles children layout algorithms for {@link visreed.view.KleeneStarNodeView}
 * @author Xiaoyu Guo
 */
public class KleeneStarLayoutManager extends VisreedNodeLayoutManager {

    private static final double HSPACE_PIXEL = 20;
    private static final double VSPACE_TOP_PIXEL = 10;
    private static final double VSPACE_KID_TOP_PIXEL = 20;
    private static final double VSPACE_BOTTOM_PIXEL = 0;
    protected KleeneStarLayoutManager() {}

    /* (non-Javadoc)
     * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView)
     */
    @Override
    public void layoutNode(VisreedNodeView nv) {
        if(nv == null){
            return;
        }

        // TODO assert(nv.getNumChildren() == 1)
        VisreedNodeView kid = nv.getVisreedChild(0);
        if(kid == null){
            return;
        }
        
        // check direction
        Direction kidDirection = nv.getCurrentDirection().getReverseDirection();
        if(! kid.getCurrentDirection().equals(kidDirection)){
            kid.setDirection(kidDirection);
        }
        
        double kidWidth = 0.0;
        double kidHeight = 0.0;
        if(kid != null){
            kid.doLayout();
            kidWidth = kid.getNextShapeExtent().getWidth();
            kidHeight = kid.getNextShapeExtent().getHeight();
        }
        
        nv.placeNext(0, 0);
        kid.placeNextHierarchy(HSPACE_PIXEL, VSPACE_KID_TOP_PIXEL);

        // set the padding
        Rectangle2D myNextExtent = new Rectangle2D.Double(
            0, 
            0, 
            kidWidth + 2 * HSPACE_PIXEL, 
            kidHeight + VSPACE_KID_TOP_PIXEL + VSPACE_BOTTOM_PIXEL
        );
        nv.setNextShape(myNextExtent);
    }
    
    private static KleeneStarLayoutManager instance = new KleeneStarLayoutManager();
    
    /**
     * Gets the globally unique instance of this class. (Singleton Pattern)
     * @return
     */
    public static VisreedNodeLayoutManager getInstance() {
        return instance;
    }

    public static double getVSpaceTop(){
        return VSPACE_TOP_PIXEL;
    }

    public static double getVSpaceBottom(){
        return VSPACE_BOTTOM_PIXEL;
    }
}
